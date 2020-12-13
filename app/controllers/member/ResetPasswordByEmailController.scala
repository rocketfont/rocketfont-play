package controllers.member

import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, OFormat, Reads}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.email.SendEmail
import undefined.exception.ValidationException
import undefined.slick.Tables.{Member, MemberFindPassword, MemberFindPasswordRow}

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.github.t3hnar.bcrypt._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */


case class ResetPasswordRequest(password : String, memberSrl : Long)
object ResetPasswordRequest{
  implicit val json_reds: OFormat[ResetPasswordRequest] = Json.format[ResetPasswordRequest]
}
@Singleton
class ResetPasswordByEmailController @Inject()(
                                                            val config: Configuration,
                                                            val controllerComponents: ControllerComponents,
                                                            protected val dbConfigProvider: DatabaseConfigProvider,
                                                            val ec: ExecutionContext,
                                                            val dbEc: DBExecutionContext,
                                                            val sendEmail: SendEmail
                                                          ) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  def index(token : String): Action[ResetPasswordRequest] =

    Action.async(parse.json[ResetPasswordRequest]) { implicit request =>

      val now = Timestamp.valueOf(LocalDateTime.now())
      val query = MemberFindPassword.filter(t =>
        t.memberSrl === request.body.memberSrl
        && t.token === token
        && t.expires > now
      ).delete

      val deleteResultF: Future[Int] = db.run(query)

      val passwordResetRequestResult = deleteResultF.map{
        case 0 => throw new ValidationException("유효한 token 이 아닙니다.")
        case 1 => ()
        case _ => throw new ValidationException("서버 에러입니다. 관리자에게 문의해 주세요. CODE :[1002323]")
      }(ec)

      val passwordUpdateResult = passwordResetRequestResult.flatMap{_ =>
        val newPassword = request.body.password.boundedBcrypt
        val updateQuery = Member
          .filter(t => t.memberSrl === request.body.memberSrl)
          .map(t => t.password)
          .update(newPassword)

        db.run(updateQuery)
      }(ec)


      passwordUpdateResult.transform{
        case Success(0) =>
          Success(Ok(JsonResponse(message = "비밀번호 초기화에 실패하였습니다. 매칭되는 member가 없습니다.")))
        case Success(_) =>
          Success(Ok(JsonResponse(message = "비밀번호 초기화를 정상 진행하였습니다.")))
        case Failure(e : ValidationException) =>
          Success(BadRequest(JsonResponse(message = e.getMessage )))
        case Failure(exception) =>
          Failure(exception)
      }(ec)
    }
}


