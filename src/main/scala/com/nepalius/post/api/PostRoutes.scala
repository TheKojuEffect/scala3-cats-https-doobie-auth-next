package com.nepalius.post.api

import cats.Monad
import cats.effect.Sync
import com.nepalius.auth.Auth.AuthHandler
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

object PostRoutes {

  def routes[F[_]: Sync](
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] = Router(
    "posts" -> create(authHandler),
  )

  def create[F[_]: Sync](authHandler: AuthHandler[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] { case POST -> Root =>
      Ok("Test")
    }
  }
}
