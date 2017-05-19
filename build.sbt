import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "me.lahcini",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "SimpleCrawler",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "io.lemonlabs" %% "scala-uri" % "0.4.16",
      "org.scalaj" %% "scalaj-http" % "2.3.0"
    )
  )