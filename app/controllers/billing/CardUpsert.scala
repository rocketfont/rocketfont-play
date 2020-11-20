package controllers.billing

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.libs.ws.WSClient
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse

import scala.async.Async.async
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class CardRequest(
                      cardNumber : String,
                      cardExpiry : String,
                      birthday8 : String,
                      cardPassword2 : String
                      )
object CardRequest{
  implicit val reads: Reads[CardRequest] = Json.reads[CardRequest]
}
case class ImportRequest (imp_key : String = "4715368713226369",
                          imp_secret : String = "u90tyzq2lBBGfqrnNGAu4bo7TW6wlCMQMMkllKnHmYoCpP0dwAHBM6IF2PcosYfeLzz6kXOGWcil1izy")
object ImportRequest{
  implicit val writes: OWrites[ImportRequest] =  Json.writes[ImportRequest]
}
@Singleton
class CardUpsert @Inject()(val controllerComponents: ControllerComponents,
                           protected val dbConfigProvider: DatabaseConfigProvider,
                           val authorizeAction: AuthorizedAction,
                           val ws: WSClient,
                           val ec: ExecutionContext) extends BaseController
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
      .withHttpHeaders("content-type"->"application/json")
      .post(Json.toJson(ImportRequest()))
    val tokenF = tokenRequest.map(req => (req.json \ "access_token").as[String])(ec)
    tokenF.map( token =>
    ws.url("https://api.iamport.kr/subscribe/customer/")
    )(ec)


    async {
      Ok(JsonResponse(Json.toJson("")))
    }(ec)
  }
}

