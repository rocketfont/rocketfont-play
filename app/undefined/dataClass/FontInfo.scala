package undefined.dataClass

case class FontInfo(fontSrl: Long, fontFamilyName: String,
                    fontStyle: String, fontWeight: Int, fontFileName: String) {
  def toLocalFontName: String =
    Seq(
      Some(fontFamilyName),
      normalStringToNone(fontStyle),
      w400StringToNone(fontWeight)
    ).filter(t => t.isDefined).map(t => t.get).mkString("")


  private def normalStringToNone(str: String): Option[String] = str match {
    case "normal" => None
    case t => Some(t)
  }

  private def w400StringToNone(str: Int): Option[String] = str match {
    case 400 => None
    case t => Some(s"$t")
  }


}
