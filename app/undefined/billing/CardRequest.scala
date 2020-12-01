package undefined.billing

import play.api.libs.json.{Json, Reads}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class CardRequest(
                        cardNumber: String,
                        cardExpiry: String,
                        birthday6: String,
                        cardPassword2: String,
                        cardNickname: String
                      )

object CardRequest {
  implicit val reads: Reads[CardRequest] = Json.reads[CardRequest]
}