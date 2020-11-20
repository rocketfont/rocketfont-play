package controllers.member

import java.sql.{SQLException, SQLIntegrityConstraintViolationException, Timestamp}
import java.time.LocalDateTime

import controllers.DBExecutionContext
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsNull, JsNumber, JsObject}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.exception.ValidationException
import undefined.slick.Tables.{Member, MemberRow}

import scala.async.Async.{async, await}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class SignUp @Inject()(val controllerComponents: ControllerComponents,
                       protected val dbConfigProvider: DatabaseConfigProvider,
                       val ec: ExecutionContext,
                       val dbEc: DBExecutionContext
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

    val dbResult: Future[Int] = jsonBodyResult.flatMap { req =>
      val now = Timestamp.valueOf(LocalDateTime.now())
      val dbResultF: Future[Int] = {
        db.run(Member += MemberRow(0, req.emailNormalized, req.encryptPassword, now, now))
      }
      dbResultF
    }(dbEc)

    dbResult.transform {
        case Success(value) => Success(Created(s"${value}"))
        case Failure(th: ValidationException) => Success(BadRequest(JsonResponse(message=th.getMessage)))
        case Failure(th: SQLIntegrityConstraintViolationException)
        if th.getMessage.contains("Duplicate entry") => Success(BadRequest(JsonResponse(message="이메일이 중복입니다")))
        case Failure(exception) => Success(BadRequest(JsonResponse(message="ERROR")))
      }(ec)
  }
}


