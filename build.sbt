name := """rocket-font-server"""
organization := "undefined"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, PlayAkkaHttp2Support)

scalaVersion := "2.13.3"

scalacOptions += "-Xasync"

libraryDependencies ++= Seq(guice,
  //  jdbc,
  ws, ehcache)
libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1"
libraryDependencies += "org.mariadb.jdbc" % "mariadb-java-client" % "2.7.0"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  //  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
)
libraryDependencies += "dnsjava" % "dnsjava" % "3.3.1"
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
  //  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5"
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.8.0-scalikejdbc-3.5",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0",
)

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.10.0"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0"
libraryDependencies += "software.amazon.awssdk" % "bom" % "2.15.35" pomOnly()
libraryDependencies += "software.amazon.awssdk" % "ses" % "2.15.35"
libraryDependencies += "com.sun.mail" % "javax.mail" % "1.6.2"




// TEST
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
)

/**
 * slick.codegen.SourceCodeGenerator.main(
 * Array("slick.jdbc.MySQLProfile", "org.mariadb.jdbc.Driver",
 * "jdbc:mariadb://main-server.rocketfont.net:3306/rocket_font_main_db?" +
 * "useSSL=true&user=super&password=Rocket@Font1" +
 * "&sessionVariables=@@session.net_write_timeout=7200",
 * "/Users/leedg_u/ajou/2020_02/sw_capstone/projects/rocket-font-server/slick_out", "undefined.slick")
 * )
 * File("./").toAbsolute
 */
// code generation task

import com.typesafe.config.ConfigFactory

lazy val conf = ConfigFactory.parseFile(new File("./conf/application.conf"))

lazy val slickCodeGenTask = TaskKey[Unit]("slick-gen-tables")
slickCodeGenTask := {

  val arr = Array(
    "slick.jdbc.MySQLProfile",
    conf.getString("slick.dbs.default.db.driver"),
    conf.getString("slick.dbs.default.db.url"),
    "./app/undefined/slick", "undefined.slick",
    conf.getString("slick.dbs.default.db.username"),
    conf.getString("slick.dbs.default.db.password")
  )


  (runner in Compile).value.run("slick.codegen.SourceCodeGenerator",
    (dependencyClasspath in Compile).value.files,
    arr, streams.value.log)
}