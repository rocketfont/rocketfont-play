package controllers.billing

import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._
import scalikejdbc._
import slick.jdbc.JdbcProfile
import undefined.AuthorizedAction
import undefined.billing.{ChargeDBResult, FontBillingToFontCompanyDBResult}
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class RetrieveUsageForFontCompanyController @Inject()(val controllerComponents: ControllerComponents,
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
    require(diff < 4, "3개월 이하로만 가능합니다.")
    val memberSrl = request.session.get("memberSrl").get.toLong

    val dbResultF = Future {
      DB readOnly {
        implicit session =>
          val dbResult =
            sql"""
                 |SELECT f.font_srl,
                 |       f.font_family_name,
                 |       SUM(TIMESTAMPDIFF(MINUTE, fum.created, fum.modified))                            AS TIME_COUNT,
                 |       (fp.font_price_per_minute - fp.font_comission_per_request)                       AS PAYMENT_MINUTE_PER_FONT,
                 |       SUM(TIMESTAMPDIFF(MINUTE, fum.created, fum.modified)) * fp.font_price_per_minute AS TOTAL_TIME_PRICE
                 |FROM font_usage_measure_access_log fum
                 |         JOIN font f on fum.font_srl = f.font_srl
                 |         JOIN font_price fp on f.font_srl = fp.font_srl
                 |         JOIN font_copyright fl on f.font_copyright_srl = fl.font_copyright_srl
                 |        JOIN member_font_copyright mfc on mfc.font_copyright_srl = fl.font_copyright_srl
                 |WHERE 1=1
                 |AND member_srl = ${memberSrl}
                 |AND fum.created BETWEEN $from AND $to
                 |GROUP BY f.font_srl
                 |
"""
              .stripMargin
              .map(rs =>
                FontBillingToFontCompanyDBResult(
                  fontSrl = rs.long("font_srl"),
                  fontFamilyName = rs.string("font_family_name"),
                  timeCount = rs.long("TIME_COUNT"),
                  paymentMinutePerFont = rs.long("PAYMENT_MINUTE_PER_FONT"),
                  totalTimePrice = rs.long("TOTAL_TIME_PRICE")
                ))
              .list()
              .apply()

          dbResult
      }
    }(dbEc)

    dbResultF.map(rows => {
      val data = Json.toJson(rows)
      Ok(JsonResponse(data))
    })(ec)
  }
}

