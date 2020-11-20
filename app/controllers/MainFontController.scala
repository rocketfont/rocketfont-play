package controllers

import java.net.{URL, URLDecoder}
import java.nio.charset.StandardCharsets

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import scalikejdbc._
import undefined.dataClass.{FontTuple, UnicodeSet}
import undefined.fonts.{ABUnicodes, FontUnicodes, Fonts, ValidateRequestWithReferer}
import undefined.{AccessAndUpdateURLOnDemand, FontSubset, UnicodeRangePrinter, UnicodeSetWithFile, dataClass}
import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext
import scala.reflect.io.File
import undefined.AuthorizedAction

import scala.util.matching.Regex

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class MainFontController @Inject()(val controllerComponents: ControllerComponents,
                                   config: Configuration,
                                   access: AccessAndUpdateURLOnDemand,
                                   authorizedAction: AuthorizedAction,
                                   ac: ActorSystem,
                                   implicit val ec: ExecutionContext) extends BaseController {



  private val cdnUrl = config.get[String]("rocketFont.cdnURL")

  def index(fontParams: String, urlOpt: Option[String]): Action[AnyContent] = Action { request =>

    val refererOpt = request.headers.toSimpleMap.get("referer")

    val url = urlOpt.getOrElse("")

    val isValidRequest = ValidateRequestWithReferer(refererOpt, url)
    val referer = refererOpt.getOrElse("")

    val targetUrl = (isValidRequest, urlOpt) match {
      case (true, Some(x)) if  Seq("cdn.localhost.rocketfont.net").exists(t => referer.contains(t)) =>
        access.apply(referer)
        Some(url)
      case (true, _) => Some(url)
      case (false, _) => throw new Exception("Invaild Request")
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
        case t if t == Seq.empty[Int] => Seq(None)
        case t => t.map(x => Some(x))
      }
      sql"""
           SELECT unicode
           FROM font_unicode_set_c
           WHERE 1=1
           AND unicode  NOT IN ($unicodesSqlParam)
           ORDER BY priority DESC
           """
        .map(rs => rs.int("unicode"))
        .list()
        .apply()
    }

    val groupedOrderedUnicodesByFontSrl: Map[Long, UnicodeSet] = fontsUnicodes.map(t => {

      val (fontSrl, fontUnicodes) = t

      val AUnicodes = FontUnicodes(setAPageUnicodes).intersect(fontUnicodes)
      val BUnicodes = FontUnicodes(setBPageUnicodes).intersect(fontUnicodes)
      val CUnicodes = FontUnicodes(CUnicodesSeqInDB)
        .intersect(fontUnicodes)

      val setDUnicodes = FontUnicodes(fontUnicodes.toSeq)
        .minus(AUnicodes)
        .minus(BUnicodes)
        .minus(CUnicodes)

      fontSrl -> dataClass.UnicodeSet(AUnicodes, BUnicodes, CUnicodes, setDUnicodes)
    })


    val fontSubsetTool = new FontSubset(config, ac)
    val subsettedFontFilesByFontSrl: Map[Long, UnicodeSetWithFile] = groupedOrderedUnicodesByFontSrl.map { t =>
      val (fontSrl, unicodeSet) = t
      val fontInfo = fontsMap(fontSrl)

      val groupedOrderedSubsetedFontFiles: Seq[(Seq[File], Seq[Int])] =
        unicodeSet
          .orderdGroupedUnicodes
          .map(t => (fontSubsetTool.subsetFont(fontInfo.fontFileName, t), t))
      fontSrl -> UnicodeSetWithFile(unicodeSet, groupedOrderedSubsetedFontFiles)
    }

    //write css

    // 폰트 종류당
    val css = subsettedFontFilesByFontSrl.foldLeft(Seq.empty[String]) { (sb, a) =>

      // key fontSrl
      val (fontSrl, subsettedFontFamilyFiles: UnicodeSetWithFile) = a
      val fontInfo = fontsMap(fontSrl)
      val localFontName = fontInfo.toLocalFontName

      val unicodeInfo1 =
        s"""/*
           | FontName : ${localFontName}
           | A : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.setAUnicodes.unicodeSeq)}
           | B : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.setBUnicodes.unicodeSeq)}
           | C : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.setCUnicodes.unicodeSeq)}
           | D : ${UnicodeRangePrinter.printUnicode(subsettedFontFamilyFiles.unicodeSet.setDUnicodes.unicodeSeq)}
           | */
           | """
      val unicodeInfo2 =
        s"""
          |/*
          |FontName : ${localFontName}
          | A : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.setAUnicodes.unicodeSeq)}
          | B : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.setBUnicodes.unicodeSeq)}
          | C : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.setCUnicodes.unicodeSeq)}
          | D : ${UnicodeRangePrinter.printLiteral(subsettedFontFamilyFiles.unicodeSet.setDUnicodes.unicodeSeq)}
          | */
          |""".stripMargin


      val fontSrlCss = subsettedFontFamilyFiles
        .fileWithUnicodes
        .foldLeft(Seq.empty[String]) { (sb2, subsettedFontFile) =>
          val (fontFiles, unicodeSeq: Seq[Int]) = subsettedFontFile

          val fontUrlPart = fontFiles.foldLeft(Seq.empty[String]) { (seq, fontFile) =>
            val str =
              s"""   url('$cdnUrl/subsettedFonts/${fontFile.name}') format('${fontFile.extension}')""".stripMargin
            seq :+ str
          }.mkString(",\n")


          val localFontNameNoSpace = localFontName.replaceAll(" ", "")
          val fontFace = {
            s"""@font-face {
               |  font-family: '${fontInfo.fontFamilyName}';
               |  font-style: normal;
               |  font-weight: ${fontInfo.fontWeight};
               |  font-display: swap;
               |  src: local('$localFontName'), local('$localFontNameNoSpace'),
               |$fontUrlPart;
               |  unicode-range: ${UnicodeRangePrinter.printUnicode(unicodeSeq)};
               |}
               |""".stripMargin
          }

          sb2 :+ fontFace
        }

      sb :+ unicodeInfo2 :+ fontSrlCss.mkString("\n")
    }
    Ok(css.mkString("\n")).as(CSS)
      .withHeaders(("cache-control" -> "s-maxage=120,max-age=360000"))
  }
}
