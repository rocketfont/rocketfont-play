package undefined.dataClass

import play.api.libs.json.{Json, OWrites}





case class FontDataRow(fontSrl: Long,
                       fontFamilyName: String,
                       fontStyle: String,
                       fontWeight: Int,
                       fontLicenseSrl: Long,
                       fontCopyrightSrl: Long,
                       fontCreatorSrl: Long,
                       fontPricePerRequest : Int,
                       fontPricePerMinute : Int,

){
}

object FontDataRow {
  implicit val json_writes: OWrites[FontDataRow] = Json.writes[FontDataRow]
}



