import java.io.FileInputStream
import java.lang.System

import javax.xml.stream.{XMLEventReader, XMLInputFactory}

import scala.jdk.CollectionConverters.IteratorHasAsScala
import scala.reflect.internal.util.Collections
import scala.sys.exit


object xxxxxx extends App {
  var data = Map.empty[Int, Long]
  println("sdfsdf")
  def addSeq(iter: Iterator[Int]): () = {
    iter.foreach { char: Int =>
      val oldValue: Long = data.getOrElse(char, 0)
      val newValue: Long = oldValue + 1L
      data = data + (char -> newValue)
    }
  }

  var count = 0
  val xmlInputFactory = XMLInputFactory.newInstance
  val reader: XMLEventReader = xmlInputFactory
    .createXMLEventReader(
      new FileInputStream("/Users/leedg_u/Downloads/kowiki-20200920-pages-articles-multistream.xml"))
  while (reader.hasNext) {
    val nextEvent = reader.nextEvent
    if (nextEvent.isCharacters) {
      count += 1
      val str = nextEvent.asCharacters().getData
      println(str)
      val intSeq = str.codePoints().boxed().map(t => t.toInt).iterator().asScala
      addSeq(intSeq)
      if (count % 10000 == 0) {
        println(s"processing $count")
      }
    }
  }
  ConnectionPool.singleton("jdbc:mariadb://localhost:3306/db", "root", "password")
  DB localTx  { implicit session =>
    sql"""
         |""".stripMargin

  }




}