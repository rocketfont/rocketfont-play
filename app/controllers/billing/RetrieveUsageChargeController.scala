package controllers.billing

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.temporal.ChronoUnit
import java.time.{LocalDate, LocalDateTime}

import javax.inject._
import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc._
import undefined.AuthorizedAction
import undefined.billing.{CardRequest, ChargeDBResult, ImportRequest}
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException
import undefined.slick.Tables.{MemberCreditcard, MemberCreditcardRow}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scalikejdbc._
import slick.jdbc.JdbcProfile


@Singleton
class RetrieveUsageChargeController @Inject()(val controllerComponents: ControllerComponents,
                                              protected val dbConfigProvider: DatabaseConfigProvider,
                                              val authorizeAction: AuthorizedAction,
                                              val config: Configuration,
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
  def index(fromString: String, toString: String): Action[AnyContent] = authorizeAction.async { implicit request: Request[AnyContent] =>

    val from = LocalDate.parse(fromString)
    val to = LocalDate.parse(toString)

    val diff = ChronoUnit.MONTHS.between(from, to)
    require(diff < 1, s"FROM AND TO RANGE is TOO HIGH from : '$from', to :'$to")
    val memberSrl = request.session.get("memberSrl").get.toLong

    val dbResultF = Future {
      DB readOnly {
        implicit session =>
          val dbResult =
            sql"""
            SELECT
            f.font_srl,
            f.font_family_name,

            COUNT(session) AS SESSION_COUNT,
            fp.font_price_per_request,
            COUNT(session) * fp.font_price_per_request AS TOTAL_REQUEST_PRICE,

            SUM(TIMESTAMPDIFF(MINUTE,fum.created, fum.modified)+1)  AS TIME_COUNT,
            fp.font_price_per_minute,
            SUM(TIMESTAMPDIFF(MINUTE ,fum.created, fum.modified)+1) * fp.font_price_per_minute AS TOTAL_TIME_PRICE

            from font_usage_measure_access_log fum
                JOIN font f on fum.font_srl = f.font_srl
                JOIN font_price fp on f.font_srl = fp.font_srl
                JOIN registered_hostname rh on fum.host = rh.hostname
                JOIN member m on m.member_srl = rh.member_srl
            WHERE 1=1
            AND m.member_srl= $memberSrl
            AND fum.created BETWEEN $from AND $to
            GROUP BY f.font_srl, fp.font_price_per_request, fp.font_price_per_minute
"""
              .stripMargin
              .map(rs =>
                ChargeDBResult(rs.long("font_srl"), rs.string("font_family_name"),
                  rs.long("session_count"),
                  rs.long("font_price_per_request"),
                  rs.long("TOTAL_REQUEST_PRICE"),

                  rs.long("time_count"),
                  rs.long("font_price_per_minute"),
                  rs.long("TOTAL_TIME_PRICE")
                ))
              .list()
              .apply()

          dbResult
      }
    }(dbEc)

    dbResultF.map(rows =>{
      val data = Json.toJson(rows)
      Ok(JsonResponse(data))
    })(ec)
  }
}

