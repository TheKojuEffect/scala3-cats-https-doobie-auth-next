package com.nepalius.post.api

import cats.effect.kernel.Concurrent
import cats.implicits._
import com.nepalius.auth.Auth.AuthHandler
import com.nepalius.post.domain.{Post, PostService}
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import tsec.authentication.{TSecAuthService, asAuthed}

object PostRoutes {

  def routes[F[_]: Concurrent](
      authHandler: AuthHandler[F],
      postService: PostService[F],
  ): HttpRoutes[F] = Router(
    "posts" -> create(authHandler, postService),
  )

  def create[F[_]: Concurrent](
      authHandler: AuthHandler[F],
      postService: PostService[F],
  ): HttpRoutes[F] = {
    implicit val postRequestDecoder: EntityDecoder[F, PostRequest] = jsonOf
    implicit val postEncoder: EntityEncoder[F, Post] = jsonEncoderOf

    val dsl = Http4sDsl[F]
    import dsl._

    authHandler.liftService(
      TSecAuthService { case req @ POST -> Root asAuthed user =>
        for
          postRequest <- req.request.as[PostRequest]
          post <- postService.create(postRequest, user)
          resp <- Ok(post)
        yield resp
      },
    )
  }
}
