package controllers.fonts

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.AuthorizedAction
import undefined.dataClass.{FontDataRow, JsonResponse}
import undefined.di.DBExecutionContext
import undefined.slick.Tables.{Font, FontPrice}

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext

@Singleton
class FontsController @Inject()(val controllerComponents: ControllerComponents,
                                protected val dbConfigProvider: DatabaseConfigProvider,
                                val ec: ExecutionContext,
                                val authorizedAction: AuthorizedAction,
                                val dbEc: DBExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = authorizedAction.async { implicit request: Request[AnyContent] =>
    async {
      import slick.jdbc.MySQLProfile.api._
      val allFonts = Font join FontPrice on(_.fontSrl === _.fontSrl)
      val fontsRows = await(db.run(allFonts.result))
      val fonts = fontsRows.map { joined =>
        val (font, fontPrice) = joined
                FontDataRow(font.fontSrl, font.fontFamilyName, font.fontStyle,
                font.fontWeight, font.fontLicenseSrl, font.fontCopyrightSrl,
                font.fontCreatorSrl,
                  fontPrice.fontPricePerRequest, fontPrice.fontPricePerMinute)

      }
      Ok(JsonResponse(Json.toJson(fonts)))
    }(ec)
  }
}
