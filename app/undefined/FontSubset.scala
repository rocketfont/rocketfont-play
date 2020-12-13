package undefined

import java.security.SecureRandom

import akka.actor.ActorSystem
import com.typesafe.config.Config
import javax.inject.{Inject, Singleton}
import play.api.libs.concurrent.Akka
import play.api.{Configuration, Logger}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.io.File
import scala.sys.process._
import scala.util.{Failure, Success, Try}

@Singleton
class FontSubset @Inject()(config: Configuration, ac : ActorSystem) {


  implicit val ec: ExecutionContext = ac.dispatchers.lookup("my-context")
  private val pyftsubset = "pyftsubset"
  private val logger: Logger = Logger(this.getClass)
  private val fontFormats = config.get[Seq[String]]("rocketFont.fontFormat")

  def help: Int = {
    val future = Future {
      s"$pyftsubset --help".run()
    }
    val test = Try {
      Await.result(future, 1.second)
    }
    test match {
      case Failure(exception) => throw exception
      case Success(t) => t.exitValue()
    }
  }

  def subsetFont(fontName: String, subsetChars: Seq[Int]): Seq[File] = {
    val subsetCharsString = subsetChars.sorted.map(Character.toString).mkString
    subsetFont(fontName, subsetCharsString)
  }

  private def subsetFont(fontName: String, subsetChars: String): Seq[File] = {

    val fontsDir = File(config.underlying.getString("rocketFont.fontsDir"))
    val webRootDir = File(config.underlying.getString("rocketFont.webRootDir"))

    require(fontsDir.exists && fontsDir.isDirectory, "rocketFont.fontsDir does not exists or not a dir")
    require(webRootDir.exists && webRootDir.isDirectory, "rocketFont.webRootDir does not exists or not a dir")

    val fontSaveDir = File(s"${webRootDir.path}${File.separator}/subsettedFonts/")
    require(fontSaveDir.exists && fontSaveDir.isDirectory && fontSaveDir.canWrite, "fontWriteDir is not writeable")


    val targetFontFileNameAbs = File(fontsDir.path + File.separator + fontName).toAbsolute

    require(targetFontFileNameAbs.exists
      && targetFontFileNameAbs.isFile, s"$fontName is not exists in ${targetFontFileNameAbs.path} or not a file")


    val subsetCharFile = File.makeTemp("subsetChars", ".txt").toAbsolute
    subsetCharFile.writeAll(subsetChars)

    val fileFormats = fontFormats

    val subsetCharCount = subsetChars.length

    val newFileName = s"$fontName.s$subsetCharCount.${SHA256Hash.hash(subsetChars)}"
    val newAbsFileNameWithoutExtension = s"${webRootDir.path}${File.separator}/subsettedFonts/$newFileName"

    val outputResult = fileFormats.map(format =>
      File(s"""$newAbsFileNameWithoutExtension.$format""")
    )

    outputResult.filter(f => !f.exists)
      .map(outputFile => {
      Future {
        val sb = new StringBuilder
        val processLogger = ProcessLogger(sb append _)
          val command = Seq(pyftsubset,
            targetFontFileNameAbs.path,
            s"""--text-file=${subsetCharFile.path}""",
            s"""--flavor=${outputFile.extension}""",
            s"""--output-file=${outputFile.path}"""
          )

        logger.debug( command.mkString(" "))
        val process: Process = command run processLogger
        (process, outputFile, sb)
      }(ec)
    }
    )
      .map { t =>
        Try {
          Await.result(t, 15.seconds)
        }
      }
      .map {
        case Failure(exception) => throw exception
        case Success((p, outputFile, sb)) =>
          require(p.exitValue() == 0, s" exit code '${p.exitValue()}', see error : '$sb'")
          require(outputFile.exists, s" output file '${outputFile.path}' does not exists, see err log : '$sb'")
          outputFile
      }
    subsetCharFile.delete()

    outputResult
  }

}

