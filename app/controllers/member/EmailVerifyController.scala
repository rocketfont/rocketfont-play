package controllers.member

import java.security.SecureRandom
import java.sql.{SQLIntegrityConstraintViolationException, Timestamp}
import java.time.LocalDateTime

import javax.inject._
import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.email.SendEmail
import undefined.exception.ValidationException
import undefined.slick.Tables.{Member, MemberEmailAuth, MemberEmailAuthRow, MemberRow}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class EmailVerifyController @Inject()(
                      val config : Configuration,
                        val controllerComponents: ControllerComponents,
                       protected val dbConfigProvider: DatabaseConfigProvider,
                       val ec: ExecutionContext,
                       val dbEc: DBExecutionContext,
                        val sendEmail : SendEmail
                      ) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  def index(memberSrl : Long, token : String): Action[AnyContent] = Action.async { implicit request =>

    val now = Timestamp.valueOf(LocalDateTime.now)
    val deleteEmailAuthQuery = MemberEmailAuth.filter(row =>
      row.memberSrl === memberSrl
    && row.token === token
    && row.expire > now).delete

    val updateQuery = (for{
      m <- Member
      if (m.memberSrl === memberSrl)
    }
      yield m.accountValidStatus).update("VERIFIED")


    implicit val dbec : DBExecutionContext = dbEc
    val query = (for{
      deleteCount <- deleteEmailAuthQuery
      if(deleteCount  == 1)
      _ <- updateQuery
    } yield deleteCount).transactionally

    val dbResult = db.run(query)


    dbResult.map {
      case 0 => BadRequest(JsonResponse(message = "이메일 인증 키가 틀리거나 유효시간이 만료되었습니다.."))
      case 1 => Ok(JsonResponse(message = "성공적으로 인증되었습니다."))
    }(ec)
  }
}


