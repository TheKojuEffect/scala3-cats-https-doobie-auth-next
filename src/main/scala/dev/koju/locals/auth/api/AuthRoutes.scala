package dev.koju.locals.auth.api

import cats.data.{NonEmptyList, OptionT}
import cats.effect.Sync
import cats.implicits._
import dev.koju.locals.auth.Auth.AuthHandler
import dev.koju.locals.user.domain.UserService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.{jsonOf, _}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`WWW-Authenticate`
import org.http4s.server.Router
import org.http4s.{Challenge, EntityDecoder, HttpRoutes}
import tsec.common.Verified
import tsec.passwordhashers.{PasswordHash, PasswordHasher}

object AuthRoutes {

  def routes[F[_]: Sync, A](
      userService: UserService[F],
      passwordHasher: PasswordHasher[F, A],
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] = Router(
    "login" -> logIn(userService, passwordHasher, authHandler),
  )

  def logIn[F[_]: Sync, A](
      userService: UserService[F],
      passwordHasher: PasswordHasher[F, A],
      authHandler: AuthHandler[F],
  ): HttpRoutes[F] = {
    implicit val logInRequestDecoder: EntityDecoder[F, LogInRequest] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] { case req @ POST -> Root =>
      val action = for {
        logInRequest <- OptionT.liftF(req.as[LogInRequest])
        user <- userService.getUserByEmail(logInRequest.email)
        verificationStatus <- OptionT.liftF(
          passwordHasher.checkpw(logInRequest.password, PasswordHash[A](user.password)),
        )
        _ <- OptionT.fromOption[F](verificationStatus match {
          case Verified => Option(())
          case _        => None
        })
        token <- OptionT.liftF(authHandler.authenticator.create(user.id))
      } yield (user, token)

      action.value.flatMap {
        case Some((user, token)) =>
          Ok(user.id.asJson).map(authHandler.authenticator.embed(_, token))
        case None =>
          Unauthorized(
            `WWW-Authenticate`(NonEmptyList.of(Challenge("Basic", "NepaliUS"))),
            "Invalid email or password.",
          )
      }
    }
  }
}
