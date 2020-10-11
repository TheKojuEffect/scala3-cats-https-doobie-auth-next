package dev.koju.locals.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class AppConfig(server: ServerConfig, db: DatabaseConfig)
final case class ServerConfig(host: String, port: Int)
case class DatabaseConfig(
    url: String,
    driver: String,
    user: String,
    password: String,
)

object AppConfig {
  implicit val decoder: Decoder[AppConfig] = deriveDecoder
}

object ServerConfig {
  implicit val decoder: Decoder[ServerConfig] = deriveDecoder
}

object DatabaseConfig {
  implicit val decoder: Decoder[DatabaseConfig] = deriveDecoder
}
