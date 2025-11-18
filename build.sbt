ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "RiskScala",
    version := "0.1.0-SNAPSHOT",

    libraryDependencies ++= Seq(
      "org.scalafx"   %% "scalafx"   % "24.0.2-R36",
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )



