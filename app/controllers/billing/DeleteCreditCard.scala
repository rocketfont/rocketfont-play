package controllers.billing

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException
import undefined.slick.Tables.{MemberCreditcard, MemberCreditcardRow}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class CreditCardDeleteRequest(
                                    creditCardSrl: Long,
                                  )

object CreditCardDeleteRequest {
  implicit val reads: Reads[CreditCardDeleteRequest] = Json.reads[CreditCardDeleteRequest]
}

@Singleton
class DeleteCreditCard @Inject()(val controllerComponents: ControllerComponents,
                                 protected val dbConfigProvider: DatabaseConfigProvider,
                                 val authorizeAction: AuthorizedAction,
                                 val ws: WSClient,
                                 val ec: ExecutionContext,
                                 val dbEc: DBExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[CreditCardDeleteRequest] = authorizeAction(parse.json[CreditCardDeleteRequest]).async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val deleteQuery = MemberCreditcard
      .filter(row =>
        row.creditCardSrl === request.body.creditCardSrl
          && row.memberSrl === memberSrl)
      .delete
    val deleteResultF = db.run(deleteQuery)

    deleteResultF.transform {
      case Success(value) => Success(Ok(JsonResponse()))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

