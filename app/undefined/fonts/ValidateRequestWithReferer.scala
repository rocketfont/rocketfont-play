package undefined.fonts

import java.net.URL

import scala.util.Try
import scala.util.matching.Regex

object ValidateRequestWithReferer {

  private val httpRegex: Regex = "^https?://[a-z0-9_.-]+/.*?".r
  def apply(refererOpt : Option[String], url :String) : Boolean = {
    val referer = refererOpt.getOrElse("")
    val tryJURL = Try{
      val jURL = new URL(url)
      require(Seq("http", "https").contains(jURL.getProtocol.toLowerCase()), "url protocol must be http or https")
      require(jURL.getHost.nonEmpty, "host must not be empty")
    }
    val isUrlMatch = url.startsWith(referer) && tryJURL.isSuccess

    val urlOpt = refererOpt.map(t => new URL(t))
    val refererHost = urlOpt.map(t => t.getHost).getOrElse("")

    isUrlMatch match {
      case _ if refererHost == "localhost" => true
      case true => true
      case false => false
    }

  }

}
