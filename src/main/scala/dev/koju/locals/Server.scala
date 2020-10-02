package dev.koju.locals

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import dev.koju.locals.config.AppConfig
import io.circe.config.parser
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.server.{Server => HttpServer}

import scala.concurrent.ExecutionContext.global

object Server {
  def create[F[_]: ConcurrentEffect: ContextShift: Timer]: Resource[F, HttpServer[F]] =
    for {
      conf   <- Resource.liftF(parser.decodePathF[F, AppConfig]("locals"))
      client <- BlazeClientBuilder[F](global).resource
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg       = Jokes.impl[F](client)
      httpApp = (
        Routes.helloWorldRoutes[F](helloWorldAlg) <+>
          Routes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

      server <- BlazeServerBuilder[F](global)
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(finalHttpApp)
        .resource
    } yield server
}
