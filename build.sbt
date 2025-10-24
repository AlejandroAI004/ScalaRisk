ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "RiskScala"
  )

name := "ScalaFXApp"

version := "0.1"

scalaVersion := "2.13.14" // oder deine Scala-Version

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "24.0.2-R36"
)
