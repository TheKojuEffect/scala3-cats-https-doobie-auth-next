package com.nepalius.config

final case class AppConfig(server: ServerConfig, db: DatabaseConfig)

final case class ServerConfig(host: String, port: Int)

object AppConfig {

  // Until config library is published for scala 3
  val default: AppConfig = AppConfig(
    ServerConfig("0.0.0.0", 9000),
    DatabaseConfig(
      "jdbc:postgresql://localhost:5432/nepalius",
      "postgres",
      "postgres",
      "org.postgresql.Driver",
      DatabaseConnectionsConfig(10),
    ),
  )
}
