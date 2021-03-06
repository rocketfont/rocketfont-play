package controllers.hostsAuth

import java.time.LocalDateTime
import java.util

import play.api.libs.json.{Json, OWrites}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class GetHostNameJson(registeredHostSrl: Long, hostname: String, created : LocalDateTime)

object GetHostNameJson {
  implicit val json_writes: OWrites[GetHostNameJson] = Json.writes[GetHostNameJson]
}