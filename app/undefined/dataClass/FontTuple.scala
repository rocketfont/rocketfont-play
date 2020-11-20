package undefined.dataClass

import undefined.dataClass.FontTuple.defaultWidth


case class FontTuple(fontFamilyName: String, fontWeight: Int = defaultWidth, fontStyle: String = "normal") {
}

object FontWeightInt {
  def unapply(intStr: String): Option[Int] = intStr.toIntOption
}

object FontTuple {
  val defaultWidth = 400

  def apply(fontRequestString: String): Option[FontTuple] = {
    val fontsReqStringArr = fontRequestString.split(':')
    fontsReqStringArr match {
      case Array(fontName, FontWeightInt(fontWeight), fontStyle)
      => Some(FontTuple(fontName, fontWeight, fontStyle))
      case _ => None
    }
  }
}