package undefined.fonts

case class FontUnicodes(unicodeSeq: Seq[Int]) {

  val unicodeSet: Set[Int] = unicodeSeq.toSet

  private def minus(set: Set[Int]): FontUnicodes = {
    FontUnicodes(unicodeSeq.toSet.diff(set).toSeq)
  }
  def minus(fontUnicodes: FontUnicodes) : FontUnicodes = {
    minus(fontUnicodes.unicodeSet)
  }
  def intersect(set : Set[Int]) : FontUnicodes = {
    FontUnicodes(unicodeSet.intersect(set).toSeq)
  }
}
