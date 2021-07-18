package com.nepalius.user.api

import cats.effect.Sync
import cats.implicits._
import com.nepalius.auth.Auth.AuthHandler
import com.nepalius.user.domain.{SignUpRequest, UserProfile, UserService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{EntityDecoder, HttpRoutes}
import tsec.authentication.{TSecAuthService, _}

object NormalUserRoutes {

  def routes[F[_]: Sync](
      userService: UserService[F],
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] = Router(
    "normal-users" -> (
      signUp(userService, authHandler) <+>
        update(userService, authHandler)
    ),
  )

  def signUp[F[_]: Sync](
      userService: UserService[F],
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] = {
    implicit val signUpRequestDecoder: EntityDecoder[F, SignUpRequest] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] { case req @ POST -> Root =>
      for {
        signUpRequest <- req.as[SignUpRequest]
        normalUser <- userService.signUp(signUpRequest)
        token <- authHandler.authenticator.create(normalUser.id)
        response <- Created(normalUser.id.asJson).map(authHandler.authenticator.embed(_, token))
      } yield response
    }
  }

  def update[F[_]: Sync](
      userService: UserService[F],
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] = {
    implicit val userProfileDecoder: EntityDecoder[F, UserProfile] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl._

    authHandler.liftService(
      TSecAuthService { case req @ PUT -> Root / UUIDVar(id) asAuthed user =>
        if (user.id === id)
          for {
            profile <- req.request.as[UserProfile]
            _ <- userService.updateUserProfile(id, profile)
            result <- Ok()
          } yield result
        else
          NotFound()
      },
    )
  }
}
