package controllers.hostsAuth

import java.sql.Timestamp
import java.time.LocalDateTime

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, OWrites}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException
import undefined.slick.Tables
import undefined.slick.Tables.{RegisteredHostname, RegisteredHostnameRow}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex
import scala.util.{Failure, Success}





@Singleton
class GetHostNamesController @Inject()(val controllerComponents: ControllerComponents,
                                       protected val dbConfigProvider: DatabaseConfigProvider,
                                       val authorizedAction: AuthorizedAction,
                                       val dbEc: DBExecutionContext,
                                       val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  def index(): Action[AnyContent] = authorizedAction.async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong

    val query =  RegisteredHostname
      .filter(t => t.memberSrl === memberSrl)
      .sortBy(t => t.hostname)
      .map(t => (t.registredHostSrl, t.hostname, t.created))
    val queryResult = db.run(query.result)
    val getHostNameJson = queryResult.map(rows => {
      rows.map(row => GetHostNameJson(row._1, row._2, row._3.toLocalDateTime))
    })(dbEc)




    getHostNameJson.transform{
      case Success(value) => Success(Ok(JsonResponse(Json.toJson(value))))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

