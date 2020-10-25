package undefined

import java.security.SecureRandom

import com.typesafe.config.Config
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.io.File
import scala.sys.process._
import scala.util.{Failure, Success, Try}

@Singleton
class FontSubset @Inject()(config: Configuration, implicit val ec: ExecutionContext) {

  val pyftsubset = "pyftsubset"
  private val logger: Logger = Logger(this.getClass)

  def help: Int = {
    val future = Future {
      s"$pyftsubset --help".run
    }
    val test = Try {
      Await.result(future, 1.second)
    }
    test match {
      case Failure(exception) => throw exception
      case Success(t) => t.exitValue()
    }
  }

  private def randomSuffix(): String = {
    new SecureRandom().nextLong().toHexString
  }

  def subsetFont(fontName: String, subsetChar: String): () = {

    val fontsDir = File(config.underlying.getString("rocketFont.fontsDir"))
    val webRootDir = File(config.underlying.getString("rocketFont.webRootDir"))

    require(fontsDir.exists && fontsDir.isDirectory, "rocketFont.fontsDir does not exists or not a dir")
    require(webRootDir.exists && webRootDir.isDirectory, "rocketFont.webRootDir does not exists or not a dir")


    val targetFontFileNameAbs = File(fontsDir + File.separator + fontName).toAbsolute

    require(targetFontFileNameAbs.exists
      && targetFontFileNameAbs.isFile, s"$fontName is not exists in $fontsDir or not a file")


    val subsetCharFile = File.makeTemp("subsetChars", ".txt").toAbsolute
    subsetCharFile.writeAll(subsetChar)

    val fileFormats = Seq("woff", "woff2")

    val newFileName = s"${fontName}_${SHA256Hash.hash(subsetChar)}"
    val newAbsFileNameWithoutExtension = s"${webRootDir.path}${File.separator}$newFileName"

    fileFormats.map(format =>
      Future {
        val sb = new StringBuilder
        val processLogger = ProcessLogger(sb append _)
        val outputFile = File(s"""$newAbsFileNameWithoutExtension.$format""").toAbsolute
        val command = Seq(pyftsubset,
          targetFontFileNameAbs.path,
          s"""--text-file=${subsetCharFile.path}""",
          s"""--flavor=$format""",
          s"""--output-file=${outputFile.path}"""
        )

        logger.debug(command.mkString(" "))
        val process = command run processLogger
        (process, outputFile, sb)
      }
    )
      .map { t =>
        Try {
          Await.result(t, 10.seconds)
        }
      }
      .foreach {
        case Failure(exception) => throw exception
        case Success((p, outputFile, sb)) =>
          require(p.exitValue() == 0, s" exit code '${p.exitValue()}', see error : '$sb'")
          require(outputFile.exists, s" output file '${outputFile.path}' does not exists, see err log : '$sb'")
      }
    subsetCharFile.delete()
  }
}

