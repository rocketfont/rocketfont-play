package controllers

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.CustomExecutionContext
import play.api.libs.json.{Json, OWrites}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.slick.Tables
import undefined.slick.Tables.Font

import scala.async.Async.{async, await}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@ImplementedBy(classOf[DBExecutionContextImpl])
trait DBExecutionContext extends ExecutionContext

class DBExecutionContextImpl @Inject()(system: ActorSystem)
  extends CustomExecutionContext(system, "db-context")
    with DBExecutionContext

case class FontDataRow(fontSrl: Long,
                       fontFamilyName: String,
                       fontStyle: String,
                       fontWeight: Int,
                       fontLicenseSrl: Long,
                       fontCopyrightSrl: Long,
                       fontCreatorSrl: Long
                      )

object FontDataRow {
  implicit val json_writes: OWrites[FontDataRow] = Json.writes[FontDataRow]
}

@Singleton
class FontsController @Inject()(val controllerComponents: ControllerComponents,
                                protected val dbConfigProvider: DatabaseConfigProvider,
                                val ec: ExecutionContext,
                                val authorizedAction : AuthorizedAction,
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
      val allFonts = Font.map(t => t)
      val fontsRows = await(db.run(allFonts.result))
      val fonts = fontsRows.map(t => FontDataRow(t.fontSrl, t.fontFamilyName, t.fontStyle,
        t.fontWeight, t.fontLicenseSrl, t.fontCopyrightSrl,
        t.fontCreatorSrl
      ))
      Ok(JsonResponse(Json.toJson(fonts)))
    }(ec)
  }
}

