package controllers.member

import java.sql.{SQLException, Timestamp}
import java.time.LocalDateTime

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsNull, JsNumber, JsObject}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.slick.Tables.{Member, MemberRow}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class IsLogged @Inject()(val controllerComponents: ControllerComponents,
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
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>

    val memberSrl = request.session.get("memberSrl").map(t => t.toInt)
    val JsOption = memberSrl match {
      case Some(memberSrl) => JsNumber(memberSrl)
      case None => JsNull
    }

    val memberSrlJson = JsObject(Seq("memberSrl" -> JsOption))
    Ok(JsonResponse(memberSrlJson))
  }
}


