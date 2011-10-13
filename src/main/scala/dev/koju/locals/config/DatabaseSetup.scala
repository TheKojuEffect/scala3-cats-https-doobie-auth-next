package dev.koju.locals.config

import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import cats.syntax.functor._
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

object DatabaseSetup {

  def dbTransactor[F[_]: Async: ContextShift](
      dbConfig: DatabaseConfig,
  ): Resource[F, Transactor[F]] =
    for {
      connectionContext  <- ExecutionContexts.fixedThreadPool[F](dbConfig.connections.poolSize)
      transactionContext <- ExecutionContexts.cachedThreadPool[F]
      transactor <- HikariTransactor.newHikariTransactor(
        dbConfig.driver,
        dbConfig.url,
        dbConfig.user,
        dbConfig.password,
        connectionContext,
        Blocker.liftExecutionContext(transactionContext),
      )
    } yield transactor

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
}
