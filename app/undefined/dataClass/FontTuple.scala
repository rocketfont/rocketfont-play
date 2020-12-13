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
    val fontsReqStringArr = fontRequestString.split(":")

    val fontName = fontsReqStringArr.isDefinedAt(0) match {
      case false => None
      case true => Some(fontsReqStringArr(0))
    }

    val fontWeight = fontsReqStringArr.isDefinedAt(1) match {
      case true  if fontsReqStringArr(1).forall(t => t.isDigit) => Some(fontsReqStringArr(1))
      case _ => Some("400")
    }

    val fontStyle = fontsReqStringArr.isDefinedAt(2) match {
      case false => Some("normal")
      case true => Some(fontsReqStringArr(2))
    }


    (fontName, fontWeight, fontStyle) match {
      case (Some(fontName), Some(fontWeight), Some(fontStyle)) => Some(FontTuple(fontName, fontWeight.toInt, fontStyle))
      case _ => None
    }
  }
}