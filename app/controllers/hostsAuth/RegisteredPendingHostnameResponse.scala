package controllers.hostsAuth

import java.time.LocalDateTime

import play.api.libs.json.{Json, OWrites}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */


case class RegisteredPendingHostnameResponse(
                                              pendingHostnameSrl: Long,
                                              hostname: String,
                                              dnsTXTRecord: String,
                                              expires: LocalDateTime
                                            )

object RegisteredPendingHostnameResponse {
  implicit val json_writes: OWrites[RegisteredPendingHostnameResponse] = Json.writes[RegisteredPendingHostnameResponse]
}