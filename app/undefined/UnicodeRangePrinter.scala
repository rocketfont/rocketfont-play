package undefined

import undefined.UnicodeRangePrinter.printInternal

import scala.collection.{SortedSet, mutable}

object UnicodeRangePrinter {

  def noPrint(unicodeSeq: Seq[Int]): String = {

    val sortedUnicodeSeq = unicodeSeq.sorted

    var seq = Seq.empty[String]

    var i = 0
    while (i < sortedUnicodeSeq.size) {

      var start = -1
      var end = -1
      var cond = true
      while (cond) {

        val current = sortedUnicodeSeq(i)
        if (start == -1) {
          start = current
        }

        if (sortedUnicodeSeq.isDefinedAt(i + 1)
          && current + 1 == sortedUnicodeSeq(i + 1)) {
          i = i + 1
        }
        else {
          end = sortedUnicodeSeq(i)
          cond = false
        }
      }

      val str = if (start == end) {
        s"U+${start.toHexString}"
      }
      else {
        s"U+${start.toHexString}-${end.toHexString}"
      }
      seq = seq :+ str
      i = i + 1
    }

    seq.mkString(",")
  }

  def printUnicode(unicodes: Seq[Int]) : String = {
    val unicodeSeqSeq = printInternal(unicodes)
    unicodeSeqSeq.map{
      case Seq(u)  => s"U+${u.toHexString}"
      case Seq(start, end) => s"U+${start.toHexString}-${end.toHexString}"
    }
      .mkString(",")
  }

  def printLiteral(unicodes : Seq[Int]) : String = {
    val unicodeSeqSeq = printInternal(unicodes)
    unicodeSeqSeq.map {
      case Seq(u) => s"${String.valueOf(Character.toChars(u))}"
      case Seq(start, end) => s"${String.valueOf(Character.toChars(start))}-${String.valueOf(Character.toChars(end))}"
    }
      .mkString(",")
  }



    private def printInternal(unicodeSeq: Seq[Int]): Seq[Seq[Int]] = {

    val sortedUnicodeSeq = unicodeSeq.sorted

    var seq = Seq.empty[Seq[Int]]

    var i = 0
    while (i < sortedUnicodeSeq.size) {

      var start = -1
      var end = -1
      var cond = true
      while (cond) {

        val current = sortedUnicodeSeq(i)
        if (start == -1) {
          start = current
        }

        if (sortedUnicodeSeq.isDefinedAt(i + 1)
          && current + 1 == sortedUnicodeSeq(i + 1)) {
          i = i + 1
        }
        else {
          end = sortedUnicodeSeq(i)
          cond = false
        }
      }

      val str = if (start == end) {
        Seq(start)
      }
      else {
        Seq(start, end)
      }
      seq = seq :+ str
      i = i + 1
    }

    seq
  }
}
