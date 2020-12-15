package controllers.fontUsageMeasure

import java.net.URL
import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Base64
import java.util.function.IntUnaryOperator

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Cookie, Request}
import play.shaded.ahc.io.netty.handler.codec.base64.Base64Encoder
import scalikejdbc._
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.fonts.{Fonts, ValidateRequestWithReferer}

import scala.async.Async.async
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.FunctionConverters._
import scala.util.Random


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class FontUsageMeasureController @Inject()(val controllerComponents: ControllerComponents,
                                           protected val dbConfigProvider: DatabaseConfigProvider,
                                           val ec: ExecutionContext,
                                           val dbEc : DBExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(fontParams: String, url: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    val cookieName = "fontUsageMeasureToken"
    val cookie = request.cookies.get(cookieName).getOrElse {
      val sr = new SecureRandom()
      val bytes = new Array[Byte](16)
      sr.nextBytes(bytes)
      val token = new String(Base64.getEncoder.encode(bytes)).replace('+', '_')
        .replace('/', '-')
      val cookie = Cookie(name = cookieName, value = token,
        maxAge = None, path = "", domain = None,
        secure = true, httpOnly = true, sameSite = Some(Cookie.SameSite.None))
      cookie
    }
    val session = cookie.value

    val refererOpt = request.headers.toSimpleMap.get("referer")
    val isValidRequest = ValidateRequestWithReferer(refererOpt, url)
    require(isValidRequest, "request is invalid")

    val fonts = Fonts.getFontsFromDBByFontParams(fontParams)

    val jURL = new URL(url)
    val pathPartReady: Seq[(Some[String], Option[String])] = Seq(
      (Some(""), Option(jURL.getPath)),
      (Some("?"), Option(jURL.getQuery)),
      (Some("#"), Option(jURL.getRef))
    )
    val pathPart = pathPartReady.foldLeft(Seq.empty[String]) { (before, a) =>
      val appending = a match {
        case (_, None) => ""
        case (Some(sepeprator), Some(part)) => s"$sepeprator$part"
      }
      before :+ appending
    }.mkString


    val port = if (jURL.getPort == -1) {
      jURL.getDefaultPort
    } else {
      jURL.getPort
    }

    //    val dbAction = db.run {
    //      FontUsageMeasureAccessLog ++= fonts.map(_.fontSrl).map(fontSrl => {
    //        FontUsageMeasureAccessLogRow(fontSrl = fontSrl,
    //          fontUsageMeasureAccessSrl = -1,
    //          protocol = jURL.getProtocol.toLowerCase, host = jURL.getHost.toLowerCase,
    //          port = port, path = pathPart, session = session, created = Timestamp.valueOf(LocalDateTime.now))
    //
    //      })
//  }

  val dbAction =
    Future {
      DB localTx { implicit dBsession =>
        fonts.map(_.fontSrl).map { fontSrl =>
          sql"""
               |INSERT INTO font_usage_measure_access_log
               |(font_srl,
               |protocol, host,
               |port, path,
               |session, created, modified)
               | VALUES ($fontSrl,
               | ${jURL.getProtocol.toLowerCase}, ${jURL.getHost.toLowerCase.reverse},
               | ${port}, ${pathPart},
               | $session, ${LocalDateTime.now}, ${LocalDateTime.now()})
               | ON DUPLICATE KEY
               | UPDATE modified = ${LocalDateTime.now}
               |"""
            .stripMargin.update().apply()
        }.sum
      }
    }(dbEc)
  dbAction.map(t => Ok(t.toString).withCookies(cookie))(ec)
  }
}

