package undefined.fonts

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import play.api.Configuration
import scalikejdbc._
import undefined.dataClass.{FontInfo, UnicodeSet}
import undefined.{FontSubset, UnicodeSetWithFile, dataClass}

import javax.inject.Inject
import scala.reflect.io.File
import scala.collection.parallel.CollectionConverters._


@ImplementedBy(classOf[GenerateFontFilesImpl])
trait GenerateFontFiles {
  def apply(url: String, fontInfo: Seq[FontInfo]): Map[Long, UnicodeSetWithFile]
}


class GenerateFontFilesImpl @Inject()(config: Configuration, ac: ActorSystem) extends GenerateFontFiles {
  override def apply(url: String, fontInfo: Seq[FontInfo]): Map[Long, UnicodeSetWithFile] = {


    val (setAPageUnicodes, setBPageUnicodes) = url match {
      case "" => (Seq.empty[Int], Seq.empty[Int])
      case _ => ABUnicodes.getABUnicodesByURL(Some(url))
    }

    val fontSrls = fontInfo.map(_.fontSrl)
    val fontsMap = fontInfo.map(t => t.fontSrl -> t).toMap

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

    subsettedFontFilesByFontSrl
  }
}
