package undefined.fonts

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import scalikejdbc._

import scala.math.Ordered.orderingToOrdered

object ABUnicodes {
  def getABUnicodesByURL(urlOpt: Option[String]): (Seq[Int], Seq[Int]) = {
    DB readOnly { implicit session =>

      val nowMinus1 = LocalDateTime.now().minus(1, ChronoUnit.HOURS)
      val urlAccessSrls: Seq[Long] =
        sql"""
            SELECT ua.url_access_srl
            FROM url_access_log ua
            JOIN urls u ON 1=1
            AND u.url = $urlOpt
            AND u.url_srl = ua.url_srl
            WHERE 1=1
            AND ua.created > $nowMinus1
            order by ua.created DESC
            LIMIT 100;
           """
          .map(rs => (rs.long("url_access_srl")))
          .list()
          .apply()



      val (setAUrlAccessSrl, setBUrlAccessSrls) = urlAccessSrls match {
        case a :: b => (Some(a), b)
        case t if t == Seq.empty[Int] => (None, Seq.empty[Int])
      }


      val AUnicodes: Seq[Int] = DB readOnly { implicit session =>
        sql"""
             SELECT url_access_srl, unicode
             FROM url_letter_log
             WHERE 1=1
             AND url_access_srl = $setAUrlAccessSrl
             ORDER BY COUNT DESC
             """
          .map(rs => rs.int("unicode"))
          .list()
          .apply()
      }

      val BUnicodes: Seq[Int] = DB readOnly { implicit session =>
        val AUnicoesSqlParam = if (AUnicodes.isEmpty){ Seq(None)} else AUnicodes
        val setBUrlAccessSrlsParam = if (setBUrlAccessSrls.isEmpty){ Seq(None)} else setBUrlAccessSrls
        sql"""
             SELECT unicode
             FROM url_letter_log
             WHERE 1=1
             AND url_access_srl IN ($setBUrlAccessSrlsParam)
             AND unicode NOT IN ($AUnicoesSqlParam)
             ORDER BY COUNT DESC
             """
          .map(rs => rs.int("unicode"))
          .list()
          .apply()
      }

      (AUnicodes, BUnicodes)
    }

  }

}
