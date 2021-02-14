package com.nepalius

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import com.nepalius.auth.Auth
import com.nepalius.auth.api.AuthRoutes
import com.nepalius.config.AppConfig
import com.nepalius.user.api.NormalUserRoutes
import com.nepalius.user.domain.UserService
import com.nepalius.user.repo.UserRepository
import com.nepalius.view.ViewRoutes
import com.nepalius.config.DatabaseSetup
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
