val Http4sVersion = "0.23.6"
val CirceVersion = "0.14.1"
val CirceConfigVersion = "0.8.0"
val LogbackVersion = "1.2.6"
val DoobieVersion = "1.0.0-RC1"
val PostgresVersion = "42.3.1"
val FlywayVersion = "8.0.2"
val ScalaTagsVersion = "0.10.0"
val TSecVersion = "0.4.0"
val EnumeratumVersion = "1.7.0"

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
      "com.beachape" %% "enumeratum-doobie" % EnumeratumVersion,
      "com.beachape" %% "enumeratum-circe" % EnumeratumVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  )

enablePlugins(ScalafmtPlugin)
scalacOptions += "-Wconf:any:warning-verbose"
