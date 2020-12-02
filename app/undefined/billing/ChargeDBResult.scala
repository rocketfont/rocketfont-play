package undefined.billing

import play.api.libs.json.{Json, OWrites}

case class ChargeDBResult(fontSrl: Long,
                          fontFamilyName: String,

                          sessionCount: Long,
                          fontPricePerRequest: Long,
                          totalRequestPrice: Long,

                          timeCount: Long,
                          fontPricePerMinute: Long,
                          totalTimePrice: Long,
                         )

object ChargeDBResult{
  implicit val json_writes: OWrites[ChargeDBResult] = Json.writes[ChargeDBResult]
}
