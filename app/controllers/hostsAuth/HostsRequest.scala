package controllers.hostsAuth

import play.api.libs.json.{Json, Reads}

case class HostsRequest(hosts: String)
object HostsRequest{
  implicit val jsonReads: Reads[HostsRequest] = Json.reads[HostsRequest]
}

