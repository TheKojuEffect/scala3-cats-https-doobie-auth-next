package dev.koju.locals

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import dev.koju.locals.auth.Auth
import dev.koju.locals.auth.api.AuthRoutes
import dev.koju.locals.config.{AppConfig, DatabaseSetup}
import dev.koju.locals.user.api.NormalUserRoutes
import dev.koju.locals.user.domain.UserService
import dev.koju.locals.user.repo.UserRepository
import dev.koju.locals.view.ViewRoutes
import doobie.util.ExecutionContexts
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Server => HttpServer}
import tsec.passwordhashers.jca.BCrypt

object Server {
  def create[F[_]: ConcurrentEffect: ContextShift: Timer]: Resource[F, HttpServer[F]] =
    for {
      conf <- Resource.liftF(parser.decodePathF[F, AppConfig]("nepalius"))
      serverContext <- ExecutionContexts.cachedThreadPool[F]
      transactor <- DatabaseSetup.dbTransactor(conf.db)
      userRepo = UserRepository[F](transactor)
      passwordHasher = BCrypt.syncPasswordHasher[F]
      userService = UserService(userRepo, passwordHasher)
      authHandler = Auth.authHandler(userRepo)
      httpApp = (
        ViewRoutes.index <+>
          AuthRoutes.routes(userService, passwordHasher, authHandler) <+>
          NormalUserRoutes.routes(userService, authHandler)
      ).orNotFound
      _ <- Resource.liftF(DatabaseSetup.initDb(conf.db))
      server <- BlazeServerBuilder[F](serverContext)
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server
}
