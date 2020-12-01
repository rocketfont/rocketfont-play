package controllers.member

import java.security.SecureRandom
import java.sql.{SQLException, SQLIntegrityConstraintViolationException, Timestamp}
import java.time.LocalDateTime

import javax.inject._
import play.api.{Configuration, Logger}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsNull, JsNumber, JsObject}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.email.SendEmail
import undefined.exception.ValidationException
import undefined.slick.Tables.{Member, MemberEmailAuth, MemberEmailAuthRow, MemberRow}

import scala.async.Async.{async, await}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class SignUp @Inject()(
                        val config: Configuration,
                        val controllerComponents: ControllerComponents,
                        protected val dbConfigProvider: DatabaseConfigProvider,
                        val ec: ExecutionContext,
                        val dbEc: DBExecutionContext,
                        val sendEmail: SendEmail
                      ) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  def index(): Action[MemberSignUpLoginJson] = Action(parse.json[MemberSignUpLoginJson]).async { implicit request: Request[MemberSignUpLoginJson] =>
    val jsonRequest = request.body
    val jsonBodyResult: Future[MemberSignUpLoginJson] = Future {
      jsonRequest match {
        case t if !t.isValidEmail => throw new ValidationException("이메일이 올바르지 않습니다.")
        case t => t
      }
    }(ec)

    val dbResult: Future[Unit] = jsonBodyResult.flatMap { body =>
      val now = Timestamp.valueOf(LocalDateTime.now())
      val sr = new SecureRandom()
      val token = sr.nextLong().toHexString + sr.nextLong().toHexString

      val expire = Timestamp.valueOf(LocalDateTime.now().plusHours(1))

      implicit val databaseExe: DBExecutionContext = dbEc
      val dbAction = (for {
        memberSrl <- Member.returning(Member.map(_.memberSrl)) +=
          MemberRow(0, body.emailNormalized, body.encryptPassword, "EMAIL_AUTH", now, now)
        _ <- MemberEmailAuth += MemberEmailAuthRow(0, memberSrl, token, expire, now, now)
      } yield memberSrl).transactionally
      val dbF = db.run(dbAction)


      val emailFuture =
        dbF.transform {
          case Success(memberSrl) => Success(Future {
            val rocketFontEmailVerifyPageURL = config.get[String]("rocketFont.frontendEmailVerifyPageURL")
            val authUrl = s"$rocketFontEmailVerifyPageURL/$memberSrl/$token"
            val logger = Logger(this.getClass)
            logger.info(authUrl)
            val content =
              s"""
                 |인증 메일 입니다.
                 |아래 링크를 클릭하세요.
                 |$authUrl
                 |""".stripMargin
            sendEmail(body.emailNormalized, "인증메일", content)
          })
          case Failure(ex) => Failure(ex)
        }
      emailFuture
    }(ec).flatten

    dbResult.transform {
      case Success(value) => Success(Created(s"${value}"))
      case Failure(th: ValidationException) => Success(BadRequest(JsonResponse(message = th.getMessage)))
      case Failure(th: SQLIntegrityConstraintViolationException)
        if th.getMessage.contains("Duplicate entry") => Success(BadRequest(JsonResponse(message = "이메일이 중복입니다")))
      case Failure(exception) => throw exception
    }(ec)
  }
}


