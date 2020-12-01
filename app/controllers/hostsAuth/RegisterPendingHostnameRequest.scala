package controllers.hostsAuth

import play.api.libs.json.{Json, Reads}

case class RegisterPendingHostnameRequest(hostsText: String)
object RegisterPendingHostnameRequest{
  implicit val jsonReads: Reads[RegisterPendingHostnameRequest] = Json.reads[RegisterPendingHostnameRequest]
}

