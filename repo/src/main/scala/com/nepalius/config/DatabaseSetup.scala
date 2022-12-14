package com.nepalius.config

import cats.effect.{Async, Resource, Sync}
import cats.syntax.functor.*
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

object DatabaseSetup:

  def dbTransactor[F[_]: Async](
      dbConfig: DatabaseConfig,
  ): Resource[F, Transactor[F]] =
    for
      connectionContext <- ExecutionContexts.fixedThreadPool[F](
        dbConfig.connections.poolSize,
      )
      transactor <- HikariTransactor.newHikariTransactor(
        dbConfig.driver,
        dbConfig.url,
        dbConfig.user,
        dbConfig.password,
        connectionContext,
      )
    yield transactor

  def initDb[F[_]: Sync](config: DatabaseConfig): F[Unit] =
    Sync[F]
      .delay {
        Flyway
          .configure()
          .dataSource(config.url, config.user, config.password)
          .load()
          .migrate()
      }
      .as(())
