package com.nepalius.config

final case class DatabaseConfig(
    url: String,
    user: String,
    password: String,
    driver: String,
    connections: DatabaseConnectionsConfig,
)

final case class DatabaseConnectionsConfig(poolSize: Int)
