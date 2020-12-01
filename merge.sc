import java.time.LocalDateTime

import scalikejdbc._
import org.mariadb.jdbc.Driver


ConnectionPool.singleton("jdbc:mariadb://localhost:3306/db", "root", "2password")
DB localTx { implicit session =>
  val ko_wiki_unicode =
    sql"""
         |SELECT unicode, priority
         |FROM font_unicode_set_c_ko_wiki
         |WHERE 1=1
         |ORDER by set_c_srl
         |""".stripMargin
            .map(rs => (rs.int("unicode"), rs.int("priority")))
            .list()
            .apply()

  val namu_wiki_unicode =
    sql"""
         |SELECT unicode, priority
         |FROM font_unicode_set_c_ko_namu
         |WHERE 1=1
         |ORDER by set_c_srl
         |""".stripMargin
            .map(rs => (rs.int("unicode"), rs.int("priority")))
            .list()
            .apply()

  val dataToMerge = ko_wiki_unicode ++ namu_wiki_unicode
  println("running query")
  dataToMerge.foreach(row => {
    val (unicode: Int, count: Int) = row
    sql"""
           INSERT INTO font_unicode_set_c
           (unicode, priority, created, modified)
           VALUES
           ($unicode, $count, ${LocalDateTime.now()}, ${LocalDateTime.now()})
           ON DUPLICATE KEY UPDATE
           priority = priority + ${count}
           """
  })
}