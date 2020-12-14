package controllers.hostsAuth

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.LocalDateTime

import org.xbill.DNS.Record
import org.xbill.DNS.Type
import javax.inject._
import javax.naming.directory.InitialDirContext
import org.xbill.DNS.{Lookup, SimpleResolver, TXTRecord}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import undefined.AuthorizedAction
import undefined.dataClass.JsonResponse
import undefined.di.DBExecutionContext
import undefined.exception.ValidationException
import undefined.slick.Tables.{RegisteredHostname, RegisteredHostnamePending, RegisteredHostnameRow}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class VerifyHostnameController @Inject()(val controllerComponents: ControllerComponents,
                                         protected val dbConfigProvider: DatabaseConfigProvider,
                                         val authorizedAction: AuthorizedAction,
                                         val dbEc: DBExecutionContext,
                                         val ec: ExecutionContext) extends BaseController
  with HasDatabaseConfigProvider[JdbcProfile] {

  def index(pendingHostNameSrl: Long): Action[AnyContent] = authorizedAction.async { implicit request =>

    val memberSrl = request.session.get("memberSrl").get.toLong
    val query = RegisteredHostnamePending.filter(row =>
      row.memberSrl === memberSrl
        && row.pendingHostnameSrl === pendingHostNameSrl)
    val result = db.run(query.result).map(rows => rows.head)(ec)


    val isSuccess = result.flatMap(row => {
      val dnsTxtRecord = row.dnsTxtRecord
      val txtRecords = getDNSTxtRecords(row.hostname)
      txtRecords.map(txtRecords => (row, txtRecords, txtRecords.contains(dnsTxtRecord)))(ec)
    })(ec)

    val response = isSuccess.map {
      case (row, txtRecords, true) =>
        val now = Timestamp.valueOf(LocalDateTime.now())
        val deleteQuery = RegisteredHostnamePending
          .filter(r => r.pendingHostnameSrl === row.pendingHostnameSrl)
          .delete
        val dbResult = DBIO.seq(
          deleteQuery,
            RegisteredHostname += RegisteredHostnameRow(1L, memberSrl, row.hostname.reverse, now, now)
        ).transactionally
        db.run(dbResult)
      //        Success(dbResult)
      case (row, txtRecords, false) =>
        throw new ValidationException(s"TXT 레코드가 일치 하지 않습니다. 현재 TXT레코드는 ${txtRecords.mkString(",")} 입니다.")
    }(ec)

    response.transform {
      case Success(value) => Success(Created(JsonResponse()))
      case Failure(e: ValidationException) => Success(BadRequest(JsonResponse(message = e.getMessage)))
      case Failure(e) => Failure(e)
    }(ec)
  }

  def getDNSTxtRecords(host: String): Future[Seq[String]] = Future {

    val lookup = new Lookup(host, Type.TXT)
    lookup.setResolver(new SimpleResolver("8.8.8.8"))
    val records: Array[Record] = Option(lookup.run()).getOrElse(Array())
    records.map(record => {
      val txtRecord = record.asInstanceOf[TXTRecord]
      val string = txtRecord.getStrings
      string.asScala
    }).toSeq.flatten
  }(ec)
}

