ThisBuild / organization := "com.nepalius"
ThisBuild / scalaVersion := "3.1.3"
ThisBuild / version := "0.0.1-SNAPSHOT"

val V = new {
  val CatsEffect = "3.3.13"
  val CatsEffectTime = "0.2.0"
  val Http4s = "0.23.13"
  val Circe = "0.14.2"
  val CirceConfig = "0.9.0"
  val Logback = "1.2.11"
  val Doobie = "1.0.0-RC2"
  val Postgres = "42.3.6"
  val Flyway = "8.5.13"
  val TSec = "0.4.0"
}

lazy val domain = project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % V.Logback,
      "org.typelevel" %% "cats-effect" % V.CatsEffect,
      "io.chrisdavenport" %% "cats-effect-time" % V.CatsEffectTime,
    ),
  )

lazy val repo = project
  .dependsOn(domain)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % V.Postgres,
      "org.tpolecat" %% "doobie-free" % V.Doobie,
      "org.tpolecat" %% "doobie-core" % V.Doobie,
      "org.tpolecat" %% "doobie-postgres" % V.Doobie,
      "org.tpolecat" %% "doobie-hikari" % V.Doobie,
      "org.flywaydb" % "flyway-core" % V.Flyway,
    ),
  )

lazy val api = project
  .dependsOn(domain)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-generic" % V.Circe,
      "org.http4s" %% "http4s-ember-server" % V.Http4s,
      "org.http4s" %% "http4s-circe" % V.Http4s,
      "org.http4s" %% "http4s-dsl" % V.Http4s,
      "io.github.jmcardon" %% "tsec-common" % V.TSec,
      "io.github.jmcardon" %% "tsec-password" % V.TSec,
      "io.github.jmcardon" %% "tsec-http4s" % V.TSec,
    ),
  )

lazy val root = (project in file("."))
  .settings(name := "NepaliUS")
  .settings(commonSettings)
  .aggregate(domain, api, repo)
  .settings(reStart / aggregate := false)
  .settings(
    libraryDependencies ++= Seq(
      "com.hunorkovacs" %% "circe-config" % V.CirceConfig,
    ),
  )
  .dependsOn(domain, api, repo)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-new-syntax",
    "-source:future",
  ),
)
