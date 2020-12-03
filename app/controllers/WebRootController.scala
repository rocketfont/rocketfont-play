package controllers

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Instant, ZoneId, ZonedDateTime}

import javax.inject._
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.math.Ordered.orderingToOrdered
import scala.reflect.io.File

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class WebRootController @Inject()(val controllerComponents: ControllerComponents,
                                  config: Configuration,
                                  implicit val ec: ExecutionContext) extends BaseController {

  private val webRootDir = config.get[String]("rocketFont.webRootDir")

  private val format304 = DateTimeFormatter.RFC_1123_DATE_TIME

  def index(fileName: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val ifModifiedSinceHeader = request.headers.toSimpleMap
      .get("if-modified-since")
      .map(time => ZonedDateTime.parse(time, format304))


    require(!fileName.contains(".."))
    val file = File(webRootDir + File.separator + fileName)
    file match {
      case f if f.exists =>

        val instant = Instant.ofEpochMilli(f.lastModified)
        val lastModifedFile = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        ifModifiedSinceHeader match {
          case Some(t) if t.toEpochSecond == lastModifedFile.toEpochSecond =>
            NotModified
          case _ =>
            Ok.sendFile(file.jfile).withHeaders(
              "cache-control" -> "max-age=1",
              "last-modified" -> lastModifedFile.format(DateTimeFormatter.RFC_1123_DATE_TIME)
            )
        }
      case _ => NotFound
    }
  }
}

