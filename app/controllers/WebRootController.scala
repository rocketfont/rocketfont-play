package controllers

import javax.inject._
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.reflect.io.File

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class WebRootController @Inject()(val controllerComponents: ControllerComponents,
                                  config : Configuration,
                                  implicit val ec : ExecutionContext) extends BaseController {

  private val webRootDir = config.get[String]("rocketFont.webRootDir")

  def index(fileName : String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val file = File(webRootDir + File.separator + fileName)
    file match {
      case f if f.exists => Ok.sendFile(file.jfile)
      case _ => NotFound
    }
  }
}
