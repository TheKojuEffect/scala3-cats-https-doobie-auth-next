package com.nepalius.user.api

import cats.effect.Concurrent
import cats.implicits.*
import com.nepalius.Controller
import com.nepalius.auth.Auth.AuthHandler
import com.nepalius.user.domain.{SignUpRequest, UserProfile, UserService}
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{EntityDecoder, HttpRoutes}
import tsec.authentication.{TSecAuthService, *}
import tsec.passwordhashers.PasswordHasher
import com.nepalius.location.StateCirce.given
import cats.effect.kernel.Async

object NormalUserController:

  def apply[F[_]: Async, A](
      userService: UserService[F],
      authHandler: AuthHandler[F],
      passwordHasher: PasswordHasher[F, A],
  ): Controller[F] =
    new Controller[F] {
      override val routes: HttpRoutes[F] = Router(
        "normal-users" -> (
          signUp(userService, authHandler, passwordHasher) <+>
            update(userService, authHandler)
        ),
      )
    }

  def signUp[F[_]: Async, A](
      userService: UserService[F],
      authHandler: AuthHandler[F],
      passwordHasher: PasswordHasher[F, A],
  ): HttpRoutes[F] =
    given signUpRequestDecoder: EntityDecoder[F, SignUpRequest] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] { case req @ POST -> Root =>
      for
        signUpRequest <- req.as[SignUpRequest]
        passwordHash <- passwordHasher.hashpw(signUpRequest.password)
        normalUserRequest <- signUpRequest.asNormalUser(passwordHash)
        normalUser <- userService.create(normalUserRequest)
        token <- authHandler.authenticator.create(normalUser.id)
        response <- Created(normalUser.id.asJson)
          .map(authHandler.authenticator.embed(_, token))
      yield response
    }

  def update[F[_]: Concurrent](
      userService: UserService[F],
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] =
    given userProfileDecoder: EntityDecoder[F, UserProfile] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl.*

    authHandler.liftService(
      TSecAuthService { case req @ PUT -> Root / UUIDVar(id) `asAuthed` user =>
        if user.id === id then
          for
            profile <- req.request.as[UserProfile]
            _ <- userService.updateUserProfile(id, profile)
            result <- Ok()
          yield result
        else NotFound()
      },
    )
