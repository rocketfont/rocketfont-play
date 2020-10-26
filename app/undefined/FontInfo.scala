package undefined

case class FontInfo(fontSrl: Long, fontName: String, fontFamilyName: String,
                    fontStyle : String, fontWeight: Char, fontFormat : String){
  def toFontFileName : String = s"${fontName}_-_${fontStyle}_-_$fontWeight"
  def toLocalFontName : String =
    Seq(
      fontFamilyName,
      normalStringToNone(fontStyle),
      w400StringToNone(fontWeight)
    ).mkString("")


  private def normalStringToNone(str : String) : Option[String] = str match {
    case "normal" => None
    case t => Some(t)
  }
  private def w400StringToNone(str : Char) : Option[String] = str match {
    case 400 => None
    case t => Some(s"$t")
  }



}
