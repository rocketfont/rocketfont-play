package controllers.hostsAuth

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Base64

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc._
import slick.jdbc.JdbcProfile
import undefined.dataClass.JsonResponse
import undefined.AuthorizedAction
import undefined.slick.Tables.{RegisteredHostname, RegisteredHostnamePending, RegisteredHostnamePendingRow, RegisteredHostnameRow}

import scala.async.Async.async
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex
import slick.jdbc.MySQLProfile.api._
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class RegisterPendingHostnamesController @Inject()(val controllerComponents: ControllerComponents,
                                                   protected val dbConfigProvider: DatabaseConfigProvider,
                                                   val authorizedAction: AuthorizedAction,
                                                   val dbEc: DBExecutionContext,
                                                   val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  private val domainRegex: Regex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$".r

  def index(): Action[RegisterPendingHostnameRequest] = authorizedAction(parse.json[RegisterPendingHostnameRequest]).async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val hosts = request.body
      .hostsText
      .trim.split('\n')
      .map(t => t.trim.toLowerCase).toSeq

    val validHosts = Future{
      val isValidHosts = hosts.map(h => domainRegex.matches(h))
        .forall(t => t)
      require(isValidHosts, "hostName 실패")

      if(!isValidHosts){
        throw new ValidationException("hostname이 틀립니다.")
      }
      hosts
    }(ec)

    val sr = new SecureRandom()

    val insertResult  = validHosts.map{ host =>
      val rowsToInsert = host.map{h =>
        val randomHex = sr.nextLong().toHexString + sr.nextLong().toHexString

        val timestampNow = Timestamp.valueOf(LocalDateTime.now())
        val expiresTimeStamp = Timestamp.valueOf(LocalDateTime.now().plusDays(3))
        RegisteredHostnamePendingRow(1L, memberSrl, h, randomHex, expiresTimeStamp, timestampNow, timestampNow)
      }
      db.run(
        RegisteredHostnamePending ++=  rowsToInsert
      )
    }(ec)


    insertResult.transform{
      case Success(value) => Success(Created(""))
      case Failure(e : ValidationException) => Success(BadRequest(e.getMessage))
      case Failure(e) => Failure(e)
    }(ec)
  }
}

