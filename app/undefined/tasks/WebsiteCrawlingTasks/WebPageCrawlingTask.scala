package undefined.tasks.WebsiteCrawlingTasks

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
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import java.util.stream
import scala.jdk.StreamConverters.StreamHasToScala
import scala.jdk.StreamConverters._
import scalikejdbc._
import undefined.AccessAndUpdateURLOnDemand
import undefined.fonts.{Fonts, GenerateFontFiles}


class WebPageCrawlingTask @Inject()(ws: WSClient,
                                    actorSystem: ActorSystem,
                                    @Named("some-actor") someActor: ActorRef,
                                    access: AccessAndUpdateURLOnDemand,
                                    generateFontFiles: GenerateFontFiles,
                                    protected val dbConfigProvider: DatabaseConfigProvider)(
                                     implicit executionContext: ExecutionContext
                                   )
  extends HasDatabaseConfigProvider[JdbcProfile] {
  actorSystem.scheduler.scheduleAtFixedRate(
    initialDelay = 0.second,
    interval = 5.minutes,
  ) { () =>

    val logger = Logger(this.getClass)
    val minute5 = 5
    val before5min = Timestamp.valueOf(LocalDateTime.now().minusMinutes(minute5))
    val accessLogQuery = {
      import slick.jdbc.MySQLProfile.api._

      FontUsageMeasureAccessLog
        .filter(t => t.modified < before5min)
        .map(row => (row.fontSrl, s"${row.host}://${row.host.reverse}:${row.port}${row.path}"))
        .result
    }

    val dbResult: Future[Seq[(Long, String)]] = db.run(accessLogQuery)

    val httpResponseF: Future[Seq[Unit]] = dbResult.map { rows =>
      rows.map { row =>
        val (fontSrl, url: String) = row
        logger.info(s"url access $url")
        access(url)
        val fontInfo = Fonts.getFontsFromDBByFontSrls(Seq(fontSrl))
        logger.info(s"url access $url")
        generateFontFiles(url, fontInfo)
      }
    }


  }
}
