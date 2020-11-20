package undefined.fonts

case class FontUnicodes(unicodeSeq: Seq[Int]) {
  def minus(set: Set[Int]): FontUnicodes = {
    FontUnicodes(unicodeSeq.filter(t => !set.contains(t)))
  }
  def minus(seq: Seq[Int]): FontUnicodes = {
    FontUnicodes(unicodeSeq.filter(t => !seq.contains(t)))
  }
  def minus(fontUnicodes: FontUnicodes) : FontUnicodes = {
    minus(fontUnicodes.unicodeSeq)
  }

  def intersect(set : Set[Int]) : FontUnicodes = {
    FontUnicodes(unicodeSeq.filter(set.contains))
  }
}
