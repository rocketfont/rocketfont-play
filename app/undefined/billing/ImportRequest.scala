package undefined.billing

import play.api.libs.json.{Json, OWrites}

case class ImportRequest(imp_key: String,
                         imp_secret: String)

object ImportRequest {
  implicit val writes: OWrites[ImportRequest] = Json.writes[ImportRequest]
}