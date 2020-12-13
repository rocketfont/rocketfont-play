package undefined.dataClass

import undefined.fonts.{FontUnicodeSet, FontUnicodes}

import scala.{+:, ::}
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
 setDUnicodes: FontUnicodeSet) {

  val sortedSeqA: Seq[Int] = setAUnicodes.unicodeSet.toSeq.sorted
  val sortedSeqB: Seq[Int] = setBUnicodes.unicodeSet.toSeq.sorted
  val sortedSeqC: Seq[Int] = setCUnicodes.unicodeSet.toSeq.sorted
  val sortedSeqD: Seq[Int] = setDUnicodes.unicodeSet.toSeq.sorted

  def orderdGroupedUnicodes: Seq[Seq[Int]] = {


    val CDUnicodes: Seq[Int] = setCUnicodes.unicodeSeq ++ sortedSeqD

    val result: Seq[Seq[Int]] =
      ( sortedSeqA +: sortedSeqB +: (CDUnicodes.grouped(200) ++: Nil)).filter(t => t.nonEmpty)
      result
  }
}
