package controllers.hostsAuth

import play.api.libs.json.{Json, Reads}

case class HostsRequest(hostsText: String)
object HostsRequest{
  implicit val jsonReads: Reads[HostsRequest] = Json.reads[HostsRequest]
}

