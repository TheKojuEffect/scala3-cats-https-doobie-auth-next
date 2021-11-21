val Http4sVersion = "0.23.6"
val CirceVersion = "0.14.1"
val CirceConfigVersion = "0.8.0"
val LogbackVersion = "1.2.6"
val DoobieVersion = "1.0.0-RC1"
val PostgresVersion = "42.3.1"
val FlywayVersion = "8.0.3"
val TSecVersion = "0.4.0"
val EnumeratumVersion = "1.7.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.nepalius",
    name := "NepaliUS",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "3.1.0",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.postgresql" % "postgresql" % PostgresVersion,
      "org.tpolecat" %% "doobie-free" % DoobieVersion,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "io.github.jmcardon" %% "tsec-common" % TSecVersion,
      "io.github.jmcardon" %% "tsec-password" % TSecVersion,
      "io.github.jmcardon" %% "tsec-http4s" % TSecVersion,
    ),
  )

enablePlugins(ScalafmtPlugin)

scalacOptions ++= Seq(
  "-source:3.0-migration",
)