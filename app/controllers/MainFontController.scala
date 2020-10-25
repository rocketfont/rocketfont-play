package controllers

import java.time.temporal.ChronoUnit
import java.time.{LocalDate, LocalDateTime}

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import scalikejdbc._
import undefined.{Font4Tuple, FontTuple}

import scala.concurrent.ExecutionContext
import scala.math.Ordered.orderingToOrdered
import scala.reflect.io.File

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class MainFontController @Inject()(val controllerComponents: ControllerComponents,
                                   config: Configuration,
                                   implicit val ec: ExecutionContext) extends BaseController {

  private val webRootDir = config.get[String]("rocketFont.webRootDir")

  def index(font: Seq[String]): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>

    val fonts = Seq.empty[FontTuple]
    val fontNames = fonts.map(t => t.fontName)

    val referer = Some("")


    val fontSrls = DB readOnly { implicit session =>
      val wherePart = fonts.foldLeft(sqls"") { (accum, b) => {
        accum + sqls""" OR(font_name = ${b.fontName} and font_weight = ${b.fontWeight} )${"\n"}"""
      }
      }

      sql"""
          SELECT font_srl
          FROM font
          WHERE 1=0
          OR $wherePart
         """.stripMargin
        .map(rs => rs.long("font_srl"))
        .list()
        .apply()
    }

    require(fontSrls.length == fonts.length,
      s"요청한 폰트 중 일부가 서버에 존재하지 않습니다. 요청:'${fontNames.mkString(",")}', srls : '${fontSrls.mkString(",")}'")


    val fontsUnicodes: Map[Long, Set[Int]] = DB readOnly { implicit session =>
      sql"""
            SELECT font_srl, unicode
            FROM font_unicode
            WHERE 1=1
            AND font_srl in ($fontSrls)
           """
        .map(rs => (rs.long("font_srl"), rs.int("unicode")))
        .list()
        .apply()
        .foldLeft(Map.empty[Long, Set[Int]]) { (oldMap, a) =>
          val (fontSrl, unicode) = a
          val set = oldMap.getOrElse(fontSrl, Set.empty)
          oldMap + (fontSrl -> (set + unicode))
        }
    }


    val (setAPageUnicodes, setBPageUnicodes) = DB readOnly { implicit session =>


      val nowMinus1 = LocalDateTime.now().minus(1, ChronoUnit.HOURS)
      val urlAccessSrls: Seq[(Long, LocalDateTime)] =
        sql"""
            SELECT ua.url_access_srl, ua.created
            FROM url_access_log ua
            JOIN urls u ON 1=1
            AND u.url = $referer
            AND u.url_srl = ua.url_srl
            WHERE 1=1
            AND ua.created > nowMinus1
            order by ua.created DESC
           """
          .map(rs => (rs.long("access_group_srl"), rs.localDateTime("created")))
          .list()
          .apply()

      val setAUrlAccessSrl = urlAccessSrls match {
        case t if 1 == 1
          && t.headOption.isDefined
          && t.head._2 > LocalDateTime.now.minus(10, ChronoUnit.MINUTES)
        => Some(t.head._1)
        case _ => None
      }

      val ABSetUnicodes: Map[Long, Set[Int]] =
        sql"""
              SELECT url_access_srl, unicode
              FROM url_letter_log
              WHERE 1=1
              AND url_access_srl in ($urlAccessSrls)
          """
          .map(rs => (rs.long("url_access_srl"), rs.int("unicode")))
          .list()
          .apply()
          .foldLeft(Map.empty[Long, Set[Int]]) { (oldMap, a) =>
            val (urlAccessSrl, unicode) = a
            val oldSet = oldMap.getOrElse(urlAccessSrl, Set.empty)
            oldMap + (urlAccessSrl -> (oldSet + unicode))
          }

      val (setAUnicodes: Set[Int], setBUnicodes: Iterable[Int]) =
        setAUrlAccessSrl match {
          case Some(t) if ABSetUnicodes.isDefinedAt(t) => (ABSetUnicodes(t), (ABSetUnicodes - t).values.flatten.toSet)
          case _ => (Set.empty[Int], ABSetUnicodes.values.flatten.toSet)
        }

      (setAUnicodes, setBUnicodes.diff(setAUnicodes))
    }

    val setCUnicodes = DB readOnly {implicit session =>
      sql"""
           SELECT unicode
           FROM font_unicode_set_c
           ORDER BY priority ASC
           """
        .map(rs=> rs.int("unicode"))
        .list()
        .apply()
        .toSet
    }

    val filteredUnicodes = fontsUnicodes.map(t => {
      val (fontSrl, fontUnicodes) = t
      val setAUnicodes = fontUnicodes & setAPageUnicodes
      val setBUnicodes = (fontUnicodes & setBPageUnicodes) -- setAUnicodes
      val setCUnicodes = (fontUnicodes -- setAUnicodes) -- setBUnicodes
      val setDUnicodes = ((fontUnicodes -- setAUnicodes) -- setBUnicodes) -- setCUnicodes
      (fontSrl, Font4Tuple(setAUnicodes, setBUnicodes, setCUnicodes, setDUnicodes) )
    })




    ???

  }
}
