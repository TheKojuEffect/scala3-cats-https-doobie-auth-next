package com.nepalius.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class AppConfig(server: ServerConfig, db: DatabaseConfig)
final case class ServerConfig(host: String, port: Int)
case class DatabaseConfig(
    url: String,
    user: String,
    password: String,
    driver: String,
    connections: DatabaseConnectionsConfig,
)
case class DatabaseConnectionsConfig(poolSize: Int)

object AppConfig {
  implicit val decoder: Decoder[AppConfig] = deriveDecoder
}

object ServerConfig {
  implicit val decoder: Decoder[ServerConfig] = deriveDecoder
}

object DatabaseConfig {
  implicit val decoder: Decoder[DatabaseConfig] = deriveDecoder
}

object DatabaseConnectionsConfig {
  implicit val decoder: Decoder[DatabaseConnectionsConfig] = deriveDecoder
}
