package undefined.tasks

import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.Inject
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.ws.{WSClient, WSResponse}
import slick.jdbc.JdbcProfile
import undefined.slick.Tables.{FontUsageMeasureAccessLog, FontUsageMeasureAccessLogRow}

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Named
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import java.util.stream
import scala.jdk.StreamConverters.StreamHasToScala
import scala.jdk.StreamConverters._
import scalikejdbc._
import undefined.AccessAndUpdateURLOnDemand
import undefined.fonts.{Fonts, GenerateFontFiles}


class WebPageCrawlingTask @Inject()(actorSystem: ActorSystem,
                                    access: AccessAndUpdateURLOnDemand,
                                    generateFontFiles: GenerateFontFiles,
                                    protected val dbConfigProvider: DatabaseConfigProvider)(
                                     implicit executionContext: ExecutionContext
                                   )
  extends HasDatabaseConfigProvider[JdbcProfile] {
  actorSystem.scheduler.scheduleAtFixedRate(
    initialDelay = 0.second,
    interval = 1.minutes,
  ) { () =>

    val logger = Logger(this.getClass)
    val minute5 = 5
    val before5min = Timestamp.valueOf(LocalDateTime.now().minusMinutes(minute5))
    val accessLogQuery = {
      import slick.jdbc.MySQLProfile.api._

      FontUsageMeasureAccessLog
        .filter(t =>   t.modified > before5min )
        .result
    }

    val dbResult: Future[Seq[(Long, String)]] = db.run(accessLogQuery)
      .map(rows => rows.map{row =>

        val port = (row.protocol, row.port) match {
          case ("https", 443) => ""
          case ("http", 80) => ""
          case (_, port)=> s":$port"
        }
        (row.fontSrl, s"${row.protocol}://${row.host.reverse}$port${row.path}")})

    val httpResponseF: Future[Seq[Unit]] = dbResult.map { rows =>
      logger.info(s"total ${rows.length} url found")
      rows.map { row =>
        val (fontSrl, url: String) = row
        val fontInfo = Fonts.getFontsFromDBByFontSrls(Seq(fontSrl))
        logger.info(s"url access $url, Fonts : '${fontInfo.head.fontFamilyName}''")
        access(url)
        logger.info(s"gen Fonts $url")
        generateFontFiles(url, fontInfo)
      }
    }

    Await.result(httpResponseF, 10.minutes)
    logger.info("finished")


  }
}
