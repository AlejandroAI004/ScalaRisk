ThisBuild / scalaVersion := "3.3.7"
import sbtassembly.AssemblyPlugin.autoImport.*


lazy val root = (project in file("."))
  .settings(
    name := "RiskScala",
    version := "0.1.0-SNAPSHOT",

    Compile / run / fork := false,
    Compile / run / connectInput := true,

    assembly / mainClass := Some("run"),

    assembly / test := {},

    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _ @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },

    libraryDependencies ++= Seq(
      "org.scalafx"   %% "scalafx"   % "24.0.2-R36",
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "net.codingwell" %% "scala-guice" % "7.0.0",
      "com.google.inject" % "guice"     % "7.0.0",
      "org.scala-lang.modules" %% "scala-xml" % "2.4.0",
      "com.typesafe.play" %% "play-json" % "2.10.8"
    )
  )



