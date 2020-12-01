package controllers.hostsAuth

import java.sql.Timestamp
import java.time.LocalDateTime

import javax.inject._
import javax.naming.directory.InitialDirContext
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, OWrites}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.slick.Tables
import undefined.slick.Tables.{RegisteredHostname, RegisteredHostnamePending, RegisteredHostnameRow}

import scala.concurrent.{ExecutionContext, Future}




@Singleton
class GetRegisteredPendingHostnameController @Inject()(val controllerComponents: ControllerComponents,
                                                       protected val dbConfigProvider: DatabaseConfigProvider,
                                                       val authorizedAction: AuthorizedAction,
                                                       val dbEc: DBExecutionContext,
                                                       val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  def index(): Action[AnyContent] = authorizedAction.async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val query = RegisteredHostnamePending.filter(row =>
      row.memberSrl === memberSrl)
      .result
    val dbResult = db.run(query)

     val result = dbResult.map(rows => rows.map(row => RegisteredPendingHostnameResponse(
      row.pendingHostnameSrl, row.hostname, row.dnsTxtRecord, row.expires.toLocalDateTime)
    ))(ec)

    result.map(seq => {
    Ok(JsonResponse(data = Json.toJson(seq)))
  })(ec)
  }

}

