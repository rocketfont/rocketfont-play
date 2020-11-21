package controllers.hostsAuth

import play.api.libs.json.Json

case class DeleteHostRequest(registeredHostSrl: Long)
object DeleteHostRequest{
  implicit val json_reads = Json.reads[DeleteHostRequest]
}


