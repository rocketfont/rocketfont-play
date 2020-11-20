package controllers.hostsAuth

import java.sql.Timestamp
import java.time.LocalDateTime

import controllers.DBExecutionContext
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.dataClass.JsonResponse
import undefined.AuthorizedAction
import undefined.slick.Tables.{RegisteredHostname, RegisteredHostnameRow}

import scala.async.Async.async
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex
import slick.jdbc.MySQLProfile.api._
import undefined.exception.ValidationException

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class RegisterHostNamesController @Inject()(val controllerComponents: ControllerComponents,
                                            protected val dbConfigProvider: DatabaseConfigProvider,
                                            val authorizedAction: AuthorizedAction,
                                            val dbEc: DBExecutionContext,
                                            val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  private val domainRegex: Regex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$".r

  def index(): Action[HostsRequest] = authorizedAction(parse.json[HostsRequest]).async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val hosts = request.body
      .hosts
      .trim.split('\n')
      .map(t => t.trim).toSeq

    val validHosts = Future{
      val isValidHosts = hosts.map(h => domainRegex.matches(h))
        .forall(t => t)
      require(isValidHosts, "hostName 실패")

      if(!isValidHosts){
        throw new ValidationException("hostname이 틀립니다.")
      }
      hosts
    }(ec)

    val insertResult  = validHosts.map{ host =>
      val rowsToInsert = host.map{h =>
        RegisteredHostnameRow(1L, memberSrl, h.reverse,
          Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()) )
      }
      db.run(
        RegisteredHostname ++=  rowsToInsert
      )
    }(ec)


    insertResult.transform{
      case Success(value) => Success(Created(""))
      case Failure(e : ValidationException) => Success(BadRequest(e.getMessage))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

