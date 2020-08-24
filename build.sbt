ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.usedgravitrons"
ThisBuild / organizationName := "usedgravitrons"

lazy val root = (project in file("."))
  .settings(
    name := "ugconvert",
    libraryDependencies += scalaTest % Test
  )

lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1"

libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.21"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

libraryDependencies ++= Seq(
  "com.spotify" %% "scio-core" % "0.9.3",
  "com.spotify" %% "scio-test" % "0.9.3" % "test",
  "org.apache.beam" % "beam-runners-direct-java" % "2.23.0",
  "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % "2.23.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime
)
