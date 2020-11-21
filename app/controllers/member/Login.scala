package controllers.member

import java.sql.{SQLException, Timestamp}
import java.time.LocalDateTime

import akka.util.ByteString
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsNull, JsNumber, JsObject, JsSuccess, JsValue}
import play.api.libs.streams.Accumulator
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException
import undefined.slick.Tables.{Member, MemberRow}

import scala.async.Async.await
import scala.async.Async.async
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class Login @Inject()(val controllerComponents: ControllerComponents,
                      protected val dbConfigProvider: DatabaseConfigProvider,
                      val ec: ExecutionContext,
                      val dbEc: DBExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {


  def index(): Action[MemberSignUpLoginJson] = Action(parse.json[MemberSignUpLoginJson]).async { implicit request =>

    val jsonRequest = request.body
    val jsonBodyResult: Future[MemberSignUpLoginJson] = Future {
      jsonRequest match {
        case t if !t.isValidEmail => throw new ValidationException("이메일이 올바르지 않습니다.")
        case t => t
      }
    }(ec)


    val loginResult: Future[Long] = jsonBodyResult.flatMap { jsonBody =>
      val query = Member
        .filter(t => t.email === jsonBody.email)
        .map(t => (t.memberSrl, t.password))
        .take(1)
      val dbResultF = db.run(query.result).map(t => t.headOption)(dbEc)

      dbResultF.map {
        case None => throw new ValidationException("해당 회원의 정보가 존재하지 않습니다")
        case Some((memberSrl, password)) if jsonBody.verifyPassword(password) => (memberSrl)
        case _ => throw new ValidationException("비밀번호가 틀립니다.")
      }(ec)
    }(ec)


    loginResult.transform {
      case Success(memberSrl) =>
        val number = (memberSrl)
        val memberSrlJson = JsObject(Seq("memberSrl" -> JsNumber(number)))
        Success(Ok(JsonResponse(memberSrlJson)).withSession("memberSrl" -> memberSrl.toString))
      case Failure(e: ValidationException) =>
        Success(BadRequest(JsonResponse(message = e.getMessage)))
      case Failure(exception) =>
        throw exception
    }(ec)
  }
}
