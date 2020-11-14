package undefined.dataClass

import scala.collection.SortedSet

case class UnicodeSet
(setAUnicodes : SortedSet[Int],
 setBUnicodes : SortedSet[Int],
 setCUnicodes : SortedSet[Int],
 setDUnicodes : SortedSet[Int]) {
  val orderedUnicodes: Seq[Int] = Seq.empty[Int] ++ setAUnicodes ++ setBUnicodes ++ setCUnicodes ++ setDUnicodes
  val grouping = 100
}
