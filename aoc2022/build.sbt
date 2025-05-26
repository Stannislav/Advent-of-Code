ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test

lazy val root = (project in file("."))
  .settings(
    name := "year-2022"
  )
