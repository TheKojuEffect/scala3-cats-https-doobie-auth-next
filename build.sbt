ThisBuild / organization := "com.nepalius"
ThisBuild / scalaVersion := "3.1.0"
ThisBuild / version := "0.0.1-SNAPSHOT"

val CatsEffectVersion = "3.3.0"
val Http4sVersion = "0.23.6"
val CirceVersion = "0.14.1"
val CirceConfigVersion = "0.8.0"
val LogbackVersion = "1.2.7"
val DoobieVersion = "1.0.0-RC1"
val PostgresVersion = "42.3.1"
val FlywayVersion = "8.2.0"
val TSecVersion = "0.4.0"
val EnumeratumVersion = "1.7.0"

lazy val domain = project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.typelevel" %% "cats-effect" % CatsEffectVersion,
    ),
  )

lazy val repo = project
  .dependsOn(domain)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % PostgresVersion,
      "org.tpolecat" %% "doobie-free" % DoobieVersion,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
    ),
  )

lazy val api = project
  .dependsOn(domain)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.github.jmcardon" %% "tsec-common" % TSecVersion,
      "io.github.jmcardon" %% "tsec-password" % TSecVersion,
      "io.github.jmcardon" %% "tsec-http4s" % TSecVersion,
    ),
  )

lazy val root = (project in file("."))
  .settings(name := "NepaliUS")
  .settings(commonSettings)
  .aggregate(domain, api, repo)
  .settings(reStart / aggregate := false)
  .dependsOn(domain, api, repo)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-new-syntax",
    "-source:future",
  ),
)
