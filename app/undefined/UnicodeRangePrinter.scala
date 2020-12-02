package undefined

import undefined.UnicodeRangePrinter.printInternal

import scala.::
import scala.collection.{SortedSet, mutable}

sealed trait SealedUnicodeRange
sealed case class SingleUnicode(unicode : Int) extends SealedUnicodeRange
sealed case class RangeUnicode(start : Int, end : Int) extends SealedUnicodeRange


object UnicodeRangePrinter {


  def printUnicode(unicodes: Seq[Int]): String = {
    val unicodeSeqSeq = printInternal(unicodes)
    unicodeSeqSeq.map {
      case SingleUnicode(u) => s"U+${u.toHexString}"
      case RangeUnicode(start, end) => s"U+${start.toHexString}-${end.toHexString}"
    }
      .mkString(",")
  }

  def printLiteral(unicodes: Seq[Int]): String = {
    val unicodeSeqSeq = printInternal(unicodes)
    unicodeSeqSeq.map {
      case SingleUnicode(u) => s"${String.valueOf(Character.toChars(u))}"
      case RangeUnicode(start, end) => s"${String.valueOf(Character.toChars(start))}-${String.valueOf(Character.toChars(end))}"
    }
      .mkString(",")
  }


  private def printInternal(unicodeSeq: Seq[Int]): Iterable[SealedUnicodeRange] = {

    val sortedUnicodeSeq = unicodeSeq.sorted.toArray
    var vector = Seq.empty[SealedUnicodeRange]

    var i = 0
    val size = sortedUnicodeSeq.length
    while (i < size) {

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
        SingleUnicode(start)
      }
      else {
        RangeUnicode(start, end)
      }
      vector = str +: vector
      i = i + 1
    }
    vector.reverse
  }
}

