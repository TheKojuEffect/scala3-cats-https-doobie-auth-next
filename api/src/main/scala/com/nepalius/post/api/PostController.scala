package com.nepalius.post.api

import cats.effect.Concurrent
import cats.Monad
import cats.implicits.*

import com.nepalius.Controller
import com.nepalius.auth.Auth.AuthHandler
import com.nepalius.post.domain.{Post, PostRequest, PostService}
import io.circe.generic.auto.*
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import tsec.authentication.{TSecAuthService, asAuthed}
import com.nepalius.location.StateCirce.given

final case class PostController[F[_]: Concurrent](
    authHandler: AuthHandler[F],
    postService: PostService[F],
) extends Controller[F]
    with Http4sDsl[F]:

  override val routes: HttpRoutes[F] = Router(
    "posts" -> create(),
  )

  def create(): HttpRoutes[F] =
    given EntityDecoder[F, PostRequest] = jsonOf
    given EntityEncoder[F, Post] = jsonEncoderOf

    authHandler.liftService(
      TSecAuthService { case req @ POST -> Root `asAuthed` user =>
        for
          postRequest <- req.request.as[PostRequest]
          post <- postService.create(postRequest, user)
          resp <- Ok(post)
        yield resp
      },
    )
