package com.nepalius.config

final case class AppConfig(server: ServerConfig, db: DatabaseConfig)

final case class ServerConfig(host: String, port: Int)
