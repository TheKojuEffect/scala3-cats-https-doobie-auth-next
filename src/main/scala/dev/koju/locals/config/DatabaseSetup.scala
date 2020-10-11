package dev.koju.locals.config

import java.sql.DriverManager

import cats.effect.Sync
import cats.syntax.functor._
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.{Contexts, LabelExpression, Liquibase}

object DatabaseSetup {
  def initDb[F[_]](config: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      Class.forName(config.driver)
      val connection = DriverManager.getConnection(config.url, config.user, config.password)
      val database = DatabaseFactory.getInstance.findCorrectDatabaseImplementation(
        new JdbcConnection(connection),
      )
      val liquibase =
        new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database)
      liquibase.update(new Contexts(), new LabelExpression())
    }.as(())
}
