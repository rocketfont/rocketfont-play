package controllers.fontsApi

import java.net.URL

import akka.actor.ActorSystem
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import scalikejdbc._
import undefined.dataClass.UnicodeSet
import undefined.fonts.{ABUnicodes, FontUnicodeSet, FontUnicodes, Fonts}
import undefined._
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException

import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.immutable.ParSeq
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.io.File
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class MainFontController @Inject()(val controllerComponents: ControllerComponents,
                                   config: Configuration,
                                   access: AccessAndUpdateURLOnDemand,
                                   dbEc : DBExecutionContext,
                                   authorizedAction: AuthorizedAction,
                                   ac: ActorSystem,
                                   val ec: ExecutionContext) extends BaseController {

  private val cdnUrl = config.get[String]("rocketFont.cdnURL")
  private val isPrintABCLiteralCharacterRangeInCSS = config.get[Boolean]("rocketFont.fontCSS.printABCLiteralCharacterRange")
  private val isPrintABCUnicodeRange = config.get[Boolean]("rocketFont.fontCSS.printABCUnicodeRange")

  def index(memberSrl : Long, fontParams: String, urlOpt: Option[String], fontDisplayQueryString : String): Action[AnyContent] = Action { request =>

    val refererOpt = request.headers.toSimpleMap.get("referer")
    val referer = refererOpt.getOrElse("https://hostname.invalid")

    val refererHost = new URL(referer).getHost


    val isHostnameMatchF =  Future {
      DB readOnly { implicit session =>
        sql"""
            SELECT COUNT(*) AS CNT FROM registered_hostname rh
            JOIN member m ON m.member_srl = rh.member_srl
            WHERE 1=1
            AND m.member_srl = $memberSrl
            AND rh.hostname = ${refererHost}
           """
          .map(rs => rs.int("CNT"))
          .single()
          .apply()
          .get > 0
      }
    }(dbEc)

    val hostNameMatchF = isHostnameMatchF.transform{ isHostnameMatch =>
      (refererHost, isHostnameMatch) match {
        case (rh , _) if rh.endsWith(".rocketfont.net") => Success()
        case (_, Success(false)) => Failure(new ValidationException("hostname 비일치"))
      }
    }(ec)

    Await.result(hostNameMatchF, 1.second)


    val fontDisplayAllowed = Seq("auto", "block","swap", "fallback", "optional")
    val fontDisplayInAllowed = fontDisplayAllowed.contains(fontDisplayQueryString)
    val fontDisplay = if(fontDisplayInAllowed) fontDisplayQueryString else "swap"

    val url = urlOpt.getOrElse("")


    val targetUrl = urlOpt match {
      case Some(x) if  Seq("https://cdn.localhost.rocketfont.net/api/v1/fontFiles").exists(t => referer.startsWith(t)) =>
        access.apply(referer)
        Some(url)
      case Some(x) => Some(url)
      case None => None
    }


    val (setAPageUnicodes, setBPageUnicodes) = urlOpt match {
      case Some(t) =>
        ABUnicodes.getABUnicodesByURL(targetUrl)
      case None => (Seq.empty[Int], Seq.empty[Int])
    }


    val fonts = Fonts.getFontsFromDBByFontParams(fontParams)

    val fontSrls = fonts.map(_.fontSrl)
    val fontsMap = fonts.map(t => t.fontSrl -> t).toMap



    val fontsUnicodes: Map[Long, Set[Int]] = Fonts.getFontsUncidoesFromDB(fontSrls)


    require((setAPageUnicodes.toSet & setBPageUnicodes.toSet).isEmpty, "setAPageUnicodes Intersection with setBPageUnicodes NOT empty")

    val CUnicodesSeqInDB: Seq[Int] = DB readOnly { implicit session =>


      val ABunicodes = setAPageUnicodes ++ setBPageUnicodes
      val unicodesSqlParam = ABunicodes match {
        case t if t.isEmpty => Seq(-1)
        case t => t.map(x => Some(x))
      }

      sql"""
           SELECT unicode
           FROM font_unicode_set_c
           WHERE 1=1
           AND unicode  NOT IN ($unicodesSqlParam)
           ORDER BY priority DESC, unicode
           """
        .map(rs => rs.int("unicode"))
        .list()
        .apply()
    }



    val groupedOrderedUnicodesByFontSrl: Map[Long, UnicodeSet] = fontsUnicodes.map(t => {

      val (fontSrl, fontUnicodeSet) = t
      val AUnicodes = new FontUnicodes(setAPageUnicodes).intersect(fontUnicodeSet)
      val BUnicodes = new FontUnicodes(setBPageUnicodes).intersect(fontUnicodeSet)
      val CUnicodes = new FontUnicodes(CUnicodesSeqInDB)
        .intersect(fontUnicodeSet)

      val setDUnicodes = new FontUnicodeSet(fontUnicodeSet)
        .minus(AUnicodes)
        .minus(BUnicodes)
        .minus(CUnicodes)
      fontSrl -> dataClass.UnicodeSet(AUnicodes, BUnicodes, CUnicodes, setDUnicodes)
    })


    val subsettedFontFilesByFontSrl: Map[Long, UnicodeSetWithFile] = groupedOrderedUnicodesByFontSrl.map { t =>
      val (fontSrl, unicodeSet) = t
      val fontInfo = fontsMap(fontSrl)

      val fontSubsetTool = new FontSubset(config, ac)

      val groupedOrderedSubsetedFontFiles: Seq[(Seq[File], Seq[Int])] =
        unicodeSet
          .orderdGroupedUnicodes
          .par
          .map(t => (fontSubsetTool.subsetFont(fontInfo.fontFileName, t), t))
          .seq
      fontSrl -> UnicodeSetWithFile(unicodeSet, groupedOrderedSubsetedFontFiles)
    }



    //write css

    // 폰트 종류당
    var idx = 0;
    val css = subsettedFontFilesByFontSrl.foldLeft(Seq.empty[String]) { (sb, a) =>

      // key fontSrl
      val (fontSrl, subsettedFontFamilyFiles: UnicodeSetWithFile) = a
      val fontInfo = fontsMap(fontSrl)
      val localFontName = fontInfo.toLocalFontName

      /**
       * val isPrintABCLiteralCharacterRangeInCSS = config.get[Boolean]("rocketFont.fontCSS.printABCLiteralCharacterRange")
       * val isPrintABCUnicodeRange = c
       */
      val unicodeInfoRange = if(isPrintABCUnicodeRange ) {
        s"""/*
           | FontName : ${localFontName}
           | Unicodes :
           | A : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.sortedSeqA)}
           | B : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.sortedSeqB)}
           | C : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.sortedSeqC)}
           | D : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.sortedSeqD)}
           |*/
           | """.stripMargin
      } else { ""}

      val unicodeInfoCharacter = if(isPrintABCLiteralCharacterRangeInCSS) {
        s"""
           |/*
           | FontName : ${localFontName}
           | Literal :
           | A : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.sortedSeqA)}
           | B : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.sortedSeqB)}
           | C : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.sortedSeqC)}
           | D : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.sortedSeqD)}
           |*/
           |""".stripMargin
        }
      else { ""}

//
      val fontSrlCss = subsettedFontFamilyFiles
        .fileWithUnicodes
        .foldLeft(Seq.empty[String]) { (sb2, subsettedFontFile) =>
          val (fontFiles, unicodeSeq: Seq[Int]) = subsettedFontFile

          val fontUrlPart = fontFiles.foldLeft(Seq.empty[String]) { (sb, fontFile) =>
            val str =
              s"""   url('$cdnUrl/subsettedFonts/${fontFile.name}') format('${fontFile.extension}')""".stripMargin
            sb :+ (str)
          }.mkString("\n")


          idx +=1
          val localFontNameNoSpace = localFontName.replaceAll(" ", "")
          val fontFace =
            s"""
               |/* [$idx] */
               |@font-face {
               |  font-family: '${fontInfo.fontFamilyName}';
               |  font-style: normal;
               |  font-weight: ${fontInfo.fontWeight};
               |  font-display: ${fontDisplay};
               |  src: local('$localFontName'), local('$localFontNameNoSpace'),
               |$fontUrlPart;
               |  unicode-range: ${UnicodeRangePrinter.printUnicode(unicodeSeq)};
               |}
               |""".stripMargin

          fontFace +: sb2
        }
      fontSrlCss ++: (unicodeInfoRange +: (unicodeInfoCharacter +: sb))
    }
    Ok(css.reverse.mkString("\n")).as(CSS)
      .withHeaders(("cache-control" -> "s-maxage=120,max-age=864000"))
  }
}
