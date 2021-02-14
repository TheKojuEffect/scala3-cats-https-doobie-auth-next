val Http4sVersion = "0.21.7"
val CirceVersion = "0.13.0"
val CirceConfigVersion = "0.8.0"
val LogbackVersion = "1.2.3"
val DoobieVersion = "0.9.2"
val PostgresVersion = "42.2.16"
val FlywayVersion = "7.0.4"
val ScalaTagsVersion = "0.9.1"
val TSecVersion = "0.2.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.nepalius",
    name := "NepaliUS",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-scalatags" % Http4sVersion,
      "org.postgresql" % "postgresql" % PostgresVersion,
      "org.tpolecat" %% "doobie-core" % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "com.lihaoyi" %% "scalatags" % ScalaTagsVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-config" % CirceConfigVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "io.github.jmcardon" %% "tsec-common" % TSecVersion,
      "io.github.jmcardon" %% "tsec-password" % TSecVersion,
      "io.github.jmcardon" %% "tsec-http4s" % TSecVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  )

enablePlugins(ScalafmtPlugin)
