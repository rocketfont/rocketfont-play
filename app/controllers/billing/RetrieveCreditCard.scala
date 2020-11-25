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

case class CardResponse(creditCardSrl : Long, cardNickname : String)
object CardResponse{
  implicit val json_writes: OWrites[CardResponse] = Json.writes[CardResponse]
}
@Singleton
class RetrieveCreditCard @Inject()(val controllerComponents: ControllerComponents,
                                   protected val dbConfigProvider: DatabaseConfigProvider,
                                   val authorizeAction: AuthorizedAction,
                                   val ws: WSClient,
                                   val ec: ExecutionContext,
                                   val dbEc : DBExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = authorizeAction.async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val query = MemberCreditcard
      .filter(t => t.memberSrl === memberSrl)
      .map(t => (t.creditCardSrl, t.cardNickname))
      .result
     val queryResult = db.run(query)
    val cardResponseSeqF = queryResult.map{rows =>
      rows.map { row =>
        CardResponse(row._1, row._2)
      }
    }(ec)

    cardResponseSeqF.transform{
      case Success(cardResponseSeq) => Success(Ok(JsonResponse(Json.toJson(cardResponseSeq))))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

