val scala3Version = "3.4.2"

val http4sVersion = "0.23.27"

lazy val root = project
  .in(file("."))
  .settings(
    name := "exploding-kittens-calculator",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.latestbit" %% "circe-tagged-adt-codec" % "0.11.0",
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "ch.qos.logback" % "logback-classic" % "1.5.6" % Runtime,
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "io.circe" %% "circe-literal" % "0.14.7" % Test,
    ),
  )
