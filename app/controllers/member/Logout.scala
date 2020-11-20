package controllers.member

import controllers.DBExecutionContext
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsNumber, JsObject}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.exception.ValidationException
import undefined.slick.Tables.Member

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class Logout @Inject()(val controllerComponents: ControllerComponents,
                       protected val dbConfigProvider: DatabaseConfigProvider,
                       val ec: ExecutionContext,
                       val authorizedAction : AuthorizedAction,
                       val dbEc: DBExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  def index(): Action[AnyContent] = Action { implicit request =>
    Ok.withSession("empty"->"")
  }
}
