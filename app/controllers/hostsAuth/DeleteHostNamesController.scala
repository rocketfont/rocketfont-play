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
import undefined.slick.Tables.RegisteredHostname

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


@Singleton
class DeleteHostNamesController @Inject()(val controllerComponents: ControllerComponents,
                                          protected val dbConfigProvider: DatabaseConfigProvider,
                                          val authorizedAction: AuthorizedAction,
                                          val dbEc: DBExecutionContext,
                                          val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  def index(): Action[DeleteHostRequest] = authorizedAction(parse.json[DeleteHostRequest]).async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val registeredHostSrl = request.body.registeredHostSrl

    val query =  RegisteredHostname
      .filter(r =>
        r.memberSrl === memberSrl
          && r.registredHostSrl === registeredHostSrl).delete
    val queryResult = db.run(query)


    queryResult.transform{
      case Success(value) => Success(Ok(JsonResponse()))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

