package undefined

import undefined.FontTuple.defaultWidth

import scala.util.Try


case class FontTuple(fontFamilyName: String,
                     fontStyle: String = "normal", fontWeight: Int = defaultWidth) {
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
      => Some(FontTuple(fontName, fontStyle, fontWeight))
      case _ => None
    }
  }
}