package com.nepalius

import cats.effect.Resource
import cats.effect.kernel.Async
import cats.implicits._
import com.nepalius.auth.Auth
import com.nepalius.auth.api.AuthRoutes
import com.nepalius.config.{AppConfig, DatabaseSetup}
import com.nepalius.post.api.PostRoutes
import com.nepalius.user.api.NormalUserRoutes
import com.nepalius.user.domain.UserService
import com.nepalius.user.repo.UserRepository
import com.nepalius.view.ViewRoutes
import io.circe.config.parser
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.{Server => HttpServer}
import tsec.passwordhashers.jca.BCrypt

object Server {
  def create[F[_]: Async]: Resource[F, HttpServer] =
    for {
      conf <- Resource.eval(parser.decodePathF[F, AppConfig]("nepalius"))
      transactor <- DatabaseSetup.dbTransactor(conf.db)
      userRepo = UserRepository[F](transactor)
      passwordHasher = BCrypt.syncPasswordHasher[F]
      userService = UserService(userRepo, passwordHasher)
      authHandler = Auth.authHandler(userRepo)
      httpApp = (
        ViewRoutes.index <+>
          AuthRoutes.routes(userService, passwordHasher, authHandler) <+>
          NormalUserRoutes.routes(userService, authHandler) <+>
          PostRoutes.routes(authHandler)
      ).orNotFound
      _ <- Resource.eval(DatabaseSetup.initDb(conf.db))
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server
}
