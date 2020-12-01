package controllers.hostsAuth

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.slick.Tables.{RegisteredHostname, RegisteredHostnamePending}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


@Singleton
class DeletePendingHostnameController @Inject()(val controllerComponents: ControllerComponents,
                                                protected val dbConfigProvider: DatabaseConfigProvider,
                                                val authorizedAction: AuthorizedAction,
                                                val dbEc: DBExecutionContext,
                                                val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  def index(pendingHostnameSrl: Long): Action[AnyContent] = authorizedAction.async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong

    val deleteQuery = RegisteredHostnamePending
      .filter(row => row.memberSrl === memberSrl
        && row.pendingHostnameSrl === pendingHostnameSrl)
      .delete

    val deleteDbResult = db.run(deleteQuery)

    deleteDbResult.transform {
      case Success(value) => Success(Ok(JsonResponse()))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

