package controllers.member

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.dataClass.JsonResponse

import scala.async.Async.async
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class Template @Inject()(val controllerComponents: ControllerComponents,
                         protected val dbConfigProvider: DatabaseConfigProvider,
                         val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    async {
      Ok(JsonResponse(Json.toJson("")))
    }(ec)
  }
}

