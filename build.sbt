name := """rocket-font-server"""
organization := "undefined"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(guice,
//  jdbc,
  ws, ehcache)
libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1"
libraryDependencies += "com.h2database" % "h2" % "1.4.200"
libraryDependencies ++= Seq(
//  "com.typesafe.play" %% "play-slick" % "5.0.0",
//  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
//  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",

)
libraryDependencies ++= Seq (
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.5.0",
//  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5"
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.8.0-scalikejdbc-3.5",
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.8.0-scalikejdbc-3.5" % "test"
)


// TEST
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"

)

