package undefined.billing

import play.api.libs.json.{Json, OFormat}

case class FontBillingToFontCompanyDBResult(fontSrl: Long,
                                            fontFamilyName: String,

                                            timeCount: Long,
                                            paymentMinutePerFont: Long,
                                            totalTimePrice: Long,
                                           )

object FontBillingToFontCompanyDBResult {
  implicit val json_format: OFormat[FontBillingToFontCompanyDBResult] = Json.format[FontBillingToFontCompanyDBResult]
}


