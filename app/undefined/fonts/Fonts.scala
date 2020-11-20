package undefined.fonts

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import javax.inject.Inject
import scalikejdbc._
import undefined.dataClass.{FontInfo, FontTuple}

object Fonts {

  def getFontsFromDBByFontParams(fontParams : String) : Seq[FontInfo] = {

    val fontRequestes = URLDecoder.decode(fontParams, StandardCharsets.UTF_8).split('/').toSeq
    val fontTuples = fontRequestes.flatten(t => FontTuple(t))
    val fontNames = fontTuples.map(t => t.fontFamilyName)

    DB readOnly { implicit session =>
      val wherePart = fontTuples.foldLeft(sqls"") { (accum, b) =>
        accum +
          sqls""" OR ( font_family_name = ${b.fontFamilyName}
                |AND font_style = ${b.fontStyle}
                |AND font_weight = ${b.fontWeight}
                |)
            """.stripMargin('|')
      }

      val fonts = sql"""
          SELECT font_srl, font_family_name, font_style, font_weight, font_file_name
          FROM font
          WHERE 1=0
          $wherePart
         """.stripMargin
        .map(rs =>
          FontInfo(fontSrl = rs.long("font_srl"),
            fontFamilyName = rs.string("font_family_name"),
            fontWeight = rs.int("font_weight").toChar,
            fontStyle = rs.string("font_style"),
            fontFileName = rs.string("font_file_name")
          )
        )
        .list()
        .apply()


      require(fontTuples.length == fonts.length,
        s"요청한 폰트 중 일부가 서버에 존재하지 않습니다. 요청:'${fontNames.mkString(",")}', srls : '${fonts.mkString(",")}'")
      fonts
    }
  }

  def getFontsUncidoesFromDB(fontSrls: Seq[Long]): Map[Long, Set[Int]] = {
    DB readOnly { implicit session =>
      val unicodes = sql"""
            SELECT font_srl, unicode
            FROM font_unicode
            WHERE 1=1
            AND font_srl in ($fontSrls)
           """
        .map(rs => (rs.long("font_srl"), rs.int("unicode")))
        .list()
        .apply()


      require(unicodes.nonEmpty, s"font_uncode is empty $fontSrls")
        unicodes.foldLeft(Map.empty[Long, Set[Int]]) { (oldMap, a) =>
          val (fontSrl, unicode) = a
          val set = oldMap.getOrElse(fontSrl, Set.empty)
          oldMap + (fontSrl -> (set + unicode))
        }
    }
  }
}
