package undefined.dataClass

import undefined.fonts.FontUnicodes

import scala.::
import scala.collection.{SortedSet, mutable}

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
