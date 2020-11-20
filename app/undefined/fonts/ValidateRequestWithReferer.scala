package undefined.fonts

import java.net.URL

import scala.util.matching.Regex

object ValidateRequestWithReferer {

  private val httpRegex: Regex = "^https?://[a-z0-9_.-]+/.*?".r
  def apply(refererOpt : Option[String], url :String) : Boolean = {
    val referer = refererOpt.getOrElse("")
    val isUrlMatch = url.startsWith(referer) && httpRegex.matches(url.toLowerCase())

    val urlOpt = refererOpt.map(t => new URL(t))
    val refererHost = urlOpt.map(t => t.getHost).getOrElse("")

    isUrlMatch match {
      case _ if refererHost == "localhost" => true
      case true => true
      case false => false
    }

  }

}
