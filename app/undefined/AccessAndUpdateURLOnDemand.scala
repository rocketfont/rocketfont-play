package undefined

import java.time.LocalDateTime
import java.util.stream.Collectors

import com.google.inject.ImplementedBy
import javax.inject.Inject
import org.jsoup.Jsoup
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.jdk.CollectionConverters.{IteratorHasAsScala, ListHasAsScala}
import scala.async.Async.{async, await}
import scala.concurrent.duration.DurationInt

@ImplementedBy(classOf[AccessAndUpdateURLOnDemandImplementation])
trait AccessAndUpdateURLOnDemand {
  def apply(url: String): ()
}

class AccessAndUpdateURLOnDemandImplementation @Inject()
(val ws: WSClient, implicit val ec: ExecutionContext)
  extends AccessAndUpdateURLOnDemand {
  def apply(url: String): () = {
    val asynced = async {
      val res: WSResponse = await(ws.url(url).get())

      val html = res.bodyAsBytes.utf8String
      val text = Jsoup.parse(html).text()
      val unicodesWithCount: Map[Int, Int] = text.codePoints()
        .iterator()
        .asScala
        .map(t => t.toInt)
        .foldLeft(Map.empty[Int, Int]) { (prev, a) =>
          val newValue = if (prev.isDefinedAt(a)) {
            prev(a) + 1
          }
          else {
            1
          }
          prev + (a -> newValue)
        }
      unicodesInsertIntoDB(url, unicodesWithCount)
    }
    Await.result(asynced, 50.seconds)
  }

  private def unicodesInsertIntoDB(url: String, unicodeCount: Map[Int, Int]): () = {
    import scalikejdbc._

    DB localTx { implicit session =>
      sql"""
            INSERT INTO urls (url, created, modified)
            VALUES ($url, ${LocalDateTime.now()}, ${LocalDateTime.now()})
            ON DUPLICATE KEY UPDATE url_srl = LAST_INSERT_ID(url_srl), modified = ${LocalDateTime.now()} ;
           """
        .execute()
        .apply()

      val urlSrl =
        sql"""SELECT LAST_INSERT_ID() AS ID"""
          .map(rs => rs.long("ID"))
          .single()
          .apply()
          .get

      sql"""INSERT INTO url_access_log
            (url_srl, created, modified)
            VALUES
            ($urlSrl, ${LocalDateTime.now()}, ${LocalDateTime.now()});
           """.execute()
        .apply()

      val urlAccessSrl =
        sql"""SELECT LAST_INSERT_ID() AS ID"""
          .map(rs => rs.long("ID"))
          .single()
          .apply()
          .get

      val param = unicodeCount.toSeq.map(t => (Nil :+ urlAccessSrl) ++ t.productIterator.toSeq :+ LocalDateTime.now :+ LocalDateTime.now())
      sql"""
            INSERT INTO url_letter_log
            (url_access_srl, unicode, count, created, modified)
            VALUES
            (?, ?, ?, ?, ?)
           """
        .batch(param: _*)
        .apply()
    }
  }
}
