package undefined.dataClass

import undefined.fonts.FontUnicodes

import scala.::
import scala.collection.{SortedSet, mutable}

/**
 *
 * @param setAUnicodes 폰트에 있는 글자중 최근 페이지에 존재하는 글
 * @param setBUnicodes 폰트에 있는 글자중 과거 페이지에 존재하는 글자.
 * @param setCUnicodes 폰트에 있는 글자중. A, B도 아니면서 나무위키, 한국어 위키에 우선순위 데이터가 있는 글자
 * @param setDUnicodes 폰트에 있는 글자중 A, B, C도 아닌 글자. 페이지에도 없고, 나무위키, 한국어 위키에 정보가 없는 글자.
 */
case class UnicodeSet
(setAUnicodes: FontUnicodes,
 setBUnicodes: FontUnicodes,
 setCUnicodes: FontUnicodes,
 setDUnicodes: FontUnicodes) {

  def orderdGroupedUnicodes: Seq[Seq[Int]] = {

    val orderedUnicodes: Seq[Int] =
      setAUnicodes.unicodeSeq ++
        setBUnicodes.unicodeSeq ++
        setCUnicodes.unicodeSeq ++
        setDUnicodes.unicodeSeq


    val CDUnicodes = setCUnicodes.unicodeSeq ++ setDUnicodes.unicodeSeq

//    val thridUnicodesCutSize = if(ABUnicodes.size < 1200) {
//      1200-ABUnicodes.size
//    }
//    else{
//      100
//    }
//
//    val thridUnicodes = CDUnicodes.take(thridUnicodesCutSize)
//    val forthUnicodes = CDUnicodes.drop(t => t.)


    //noinspection ScalaStylek
    val result = ((Nil :+ setAUnicodes.unicodeSeq
      :+ setBUnicodes.unicodeSeq)
      ++ CDUnicodes.grouped(200)).filter(t => t.nonEmpty)

    result
  }
}
