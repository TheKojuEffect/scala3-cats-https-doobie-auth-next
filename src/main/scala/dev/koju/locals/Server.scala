package dev.koju.locals

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import dev.koju.locals.config.AppConfig
import dev.koju.locals.user.api.NormalUserRoutes
import dev.koju.locals.user.domain.UserService
import dev.koju.locals.user.repo.UserRepository
import dev.koju.locals.view.ViewRoutes
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Server => HttpServer}
import tsec.passwordhashers.jca.BCrypt

import scala.concurrent.ExecutionContext.global

object Server {
  def create[F[_]: ConcurrentEffect: ContextShift: Timer]: Resource[F, HttpServer[F]] =
    for {
      conf   <- Resource.liftF(parser.decodePathF[F, AppConfig]("locals"))
      userRepo      = UserRepository[F]()
      userService   = UserService(userRepo, BCrypt.syncPasswordHasher[F])
      httpApp = (
        ViewRoutes.index <+>
          NormalUserRoutes.routes(userService)
      ).orNotFound

      server <- BlazeServerBuilder[F](global)
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server
}
