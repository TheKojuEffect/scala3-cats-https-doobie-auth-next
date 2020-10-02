package dev.koju.locals.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class ServerConfig(host: String, port: Int)
object ServerConfig {
  implicit val decoder: Decoder[ServerConfig] = deriveDecoder
}

final case class AppConfig(server: ServerConfig)
object AppConfig {
  implicit val decoder: Decoder[AppConfig] = deriveDecoder
}
