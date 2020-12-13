package undefined.fonts

class FontUnicodes(val unicodeSeq: Seq[Int], val unicodeSet : Set[Int]) {

  def this(unicodeSeqP: Seq[Int]) = this(unicodeSeqP, unicodeSeqP.toSet)
  def this(unicodeSeqP: Set[Int]) = this(unicodeSeqP.toSeq, unicodeSeqP)

  private def minus(set: Set[Int]): FontUnicodes = {
    new FontUnicodes(unicodeSeq, unicodeSet.diff(set))
  }
  def minus(fontUnicodes: FontUnicodes) : FontUnicodes = {
    minus(fontUnicodes.unicodeSet)
  }
  def intersect(set : Set[Int]) : FontUnicodes = {
    new FontUnicodes(unicodeSeq, unicodeSet.intersect(set))
  }
}

class FontUnicodeSet(val unicodeSet : Set[Int]) {

  private def minus(set: Set[Int]): FontUnicodeSet = {
    new FontUnicodeSet(unicodeSet.diff(set))
  }
  def minus(fontUnicodes: FontUnicodes) : FontUnicodeSet = {
    minus(fontUnicodes.unicodeSet)
  }
}