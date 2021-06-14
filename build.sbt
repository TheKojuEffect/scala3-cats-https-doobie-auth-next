val Http4sVersion = "0.21.24"
val CirceVersion = "0.14.1"
val CirceConfigVersion = "0.8.0"
val LogbackVersion = "1.2.3"
val DoobieVersion = "0.13.4"
val PostgresVersion = "42.2.21"
val FlywayVersion = "7.10.0"
val ScalaTagsVersion = "0.9.4"
val TSecVersion = "0.2.1"
val EnumeratumVersion = "1.6.1"
val EnumeratumDoobieVersion = "1.6.0"
val EnumeratumCirceVersion = "1.6.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.nepalius",
    name := "NepaliUS",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.6",
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
      "com.beachape" %% "enumeratum" % EnumeratumVersion,
      "com.beachape" %% "enumeratum-doobie" % EnumeratumDoobieVersion,
      "com.beachape" %% "enumeratum-circe" % EnumeratumCirceVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  )

enablePlugins(ScalafmtPlugin)
