package controllers.billing

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json
import play.api.libs.json.{JsObject, JsString, Json, OWrites, Reads}
import play.api.libs.ws.WSClient
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.slick.Tables.{MemberCreditcard, MemberCreditcardRow}

import scala.async.Async.async
import scala.concurrent.ExecutionContext
import slick.jdbc.MySQLProfile.api._
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException

import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class CardRequest(
                        cardNumber: String,
                        cardExpiry: String,
                        birthday8: String,
                        cardPassword2: String
                      )

object CardRequest {
  implicit val reads: Reads[CardRequest] = Json.reads[CardRequest]
}

case class ImportRequest(imp_key: String = "4715368713226369",
                         imp_secret: String = "u90tyzq2lBBGfqrnNGAu4bo7TW6wlCMQMMkllKnHmYoCpP0dwAHBM6IF2PcosYfeLzz6kXOGWcil1izy")

object ImportRequest {
  implicit val writes: OWrites[ImportRequest] = Json.writes[ImportRequest]
}

@Singleton
class CardUpsert @Inject()(val controllerComponents: ControllerComponents,
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
  def index(): Action[CardRequest] = authorizeAction(parse.json[CardRequest]).async { implicit request: Request[CardRequest] =>

    val memberSrl = request.session.get("memberSrl").get.toLong

    val tokenRequest = ws.url("https://api.iamport.kr/users/getToken")
      .withHttpHeaders("content-type" -> "application/json")
      .post(Json.toJson(ImportRequest()))

    val billingKey = new SecureRandom().nextLong().toHexString


    val bodyJson = request.body
    val tokenF = tokenRequest.map{req =>
      require(req.status == 200)
      require((req.json \"code").as[Int] == 0, "code have to be 0")
      (req.json \"response" \"access_token").as[String]}(ec)

    val billingKeyRegisterF = tokenF.flatMap { token =>
      val dataToWrite = JsObject(Map(
        "card_number" -> JsString(bodyJson.cardNumber),
        "expiry" -> JsString(bodyJson.cardExpiry),
        "birth" -> JsString(bodyJson.birthday8),
        "pwd_2digt" -> JsString(bodyJson.cardPassword2)
      ))

      ws.url(s"https://api.iamport.kr/subscribe/customers/$billingKey")
        .withHttpHeaders("authorization" -> token)
        .post(dataToWrite)
    }(ec)

    val billingKeyF = billingKeyRegisterF.map { request =>

      require(request.status == 200, "status have to be 200")


      val code =(request.json \ "code").as[Int]
      val message = (request.json \ "message").asOpt[String]
      request match {
      case _ if code ==0 => billingKey
      case _ => throw new ValidationException(message.getOrElse(""))
    }}(ec)

    val cardInsertF = billingKeyF.flatMap { billingKey =>
      val row = MemberCreditcardRow(
        0, memberSrl, billingKey, bodyJson.cardNumber.takeRight(4),
        Timestamp.valueOf(LocalDateTime.now())
        , Timestamp.valueOf(LocalDateTime.now()))
      val query = MemberCreditcard.insertOrUpdate(row)
      db.run(query)
    }(dbEc)


    cardInsertF.transform{
      case Success(value) => Success(Created(""))
      case Failure(exception : ValidationException) => Success(BadRequest(JsonResponse(message = exception.getMessage)))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

