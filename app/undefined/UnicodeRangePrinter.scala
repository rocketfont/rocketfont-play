package undefined

import scala.::
import scala.collection.{SortedSet, mutable}

object UnicodeRangePrinter {

  private case class Range(start: Int, end: Int)

  def print(unicodeSeq: Seq[Int]): String = {

    var seq = Seq.empty[String]

    var i = 0
    while (i < unicodeSeq.size) {

      var start = -1
      var end = -1
      var cond = true
      while (cond) {

        val current = unicodeSeq(i)
        if (start == -1) {
          start = current
        }

        if (unicodeSeq.isDefinedAt(i + 1)
          && current + 1 == unicodeSeq(i + 1)) {
          i = i + 1
        }
        else {
          end = unicodeSeq(i)
          cond = false
        }
      }

      val str = if (start == end) {
        s"U+${start.toHexString.toUpperCase}"
      }
      else {
        s"U+${start.toHexString.toUpperCase}-${end.toHexString.toUpperCase}"
      }
      seq = seq :+ str
      i = i + 1
    }

    seq.mkString(", ")
  }

  //    unicodes.foldLeft(new StringBuilder()){(sb, a, c) =>

  //    }.toString()

}
