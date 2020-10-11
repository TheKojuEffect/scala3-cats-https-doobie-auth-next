package dev.koju.locals.config

import cats.effect.Sync
import cats.syntax.functor._
import org.flywaydb.core.Flyway

object DatabaseSetup {
  def initDb[F[_]](config: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      Flyway
        .configure()
        .dataSource(config.url, config.user, config.password)
        .load()
        .migrate()
    }.as(())
}
