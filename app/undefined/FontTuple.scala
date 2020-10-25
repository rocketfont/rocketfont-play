package undefined

case class FontTuple (fontName : String, fontWeight : Char){
  override def toString: String = s"(fontName : $fontName, fontWeight : $fontWeight)"
}
