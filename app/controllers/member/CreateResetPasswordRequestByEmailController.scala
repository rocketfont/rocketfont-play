package controllers.member

import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, Reads}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.email.SendEmail
import undefined.exception.ValidationException
import undefined.slick.Tables.{Member, MemberEmailAuth, MemberFindPassword, MemberFindPasswordRow}

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class FindPasswordByEmailHttpRequestBody(email: String)

object FindPasswordByEmailHttpRequestBody {
  implicit val json_reads: Reads[FindPasswordByEmailHttpRequestBody] = Json.reads[FindPasswordByEmailHttpRequestBody]

}

@Singleton
class CreateFindPasswordRequestByEmailController @Inject()(
                                                            val config: Configuration,
                                                            val controllerComponents: ControllerComponents,
                                                            protected val dbConfigProvider: DatabaseConfigProvider,
                                                            val ec: ExecutionContext,
                                                            val dbEc: DBExecutionContext,
                                                            val sendEmail: SendEmail
                                                          ) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  def index(): Action[FindPasswordByEmailHttpRequestBody] =
    Action.async(parse.json[FindPasswordByEmailHttpRequestBody]) { implicit request =>

      val email = request.body.email
      val memberQuery = Member.filter(t => t.email ===email)
        .map(t => t.memberSrl)
        .take(1)
        .result

      val memberSrlOptF: Future[Option[Long]] = db.run(memberQuery)
        .map(rs => rs.headOption)(ec)

      val memberSrlF = memberSrlOptF.map{
        case None => throw  new ValidationException("해당 이메일이 존재하지 않습니다.")
        case Some(memberSrl) => memberSrl
      }(ec)

      val findPasswordResult = memberSrlF.flatMap{memberSrl =>

        val sr = new SecureRandom()
        val randomToken = s"${sr.nextLong().toHexString}${sr.nextLong().toHexString}"

        val now = Timestamp.valueOf(LocalDateTime.now())
        val expiresLocalDateTime = LocalDateTime.now().plusHours(1)
        val expires = Timestamp.valueOf(expiresLocalDateTime)

        val row = MemberFindPasswordRow(
          0,
          memberSrl = memberSrl,
          token = randomToken,
          created = now,
          modified =now,
          expires = expires)

        val dbResult = db.run(MemberFindPassword += row)
        dbResult.map{_ =>
          val passwordResetLink = s"${config.get[String]("rocketFont.frontendPasswordResetURL")}/$memberSrl/$randomToken"
          val body =
            s"""비밀번호를 초기화를 요청하셨습니다.
              |아래 링크를 통해 초기화 가능합니다.
              |링크는 ${DateTimeFormatter.ISO_DATE_TIME.format(expiresLocalDateTime)}에 만료 됩니다.
              |Link : $passwordResetLink
              | """.stripMargin
          sendEmail.apply(email, "RocketFont 비밀번호 찾기",body)
        }(ec)
      }(ec)

      findPasswordResult.transform{
        case Success(_) =>
          Success(Ok(JsonResponse(message = "이메일로 비밀번호 초기화 링크를 보냈습니다. 비밀번호 초기화를 요청했습니다.")))
        case Failure(e : ValidationException) =>
          Success(BadRequest(JsonResponse(message = e.getMessage )))
        case Failure(exception) =>
          Failure(exception)
      }(ec)
    }
}


