import Dependencies._

ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.usedgravitrons"
ThisBuild / organizationName := "usedgravitrons"

lazy val root = (project in file("."))
  .settings(
    name := "ugconvert",
    libraryDependencies += scalaTest % Test
  )

libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.21"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
