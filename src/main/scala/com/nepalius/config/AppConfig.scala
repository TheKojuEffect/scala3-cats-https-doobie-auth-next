package com.nepalius.config

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
