package dev.koju.locals.user.api

import cats.effect.Sync
import cats.implicits._
import dev.koju.locals.user.domain.User.UserId
import dev.koju.locals.user.domain.{SignUpRequest, User, UserProfile, UserService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{EntityDecoder, HttpRoutes}
import tsec.authentication.{AuthEncryptedCookie, SecuredRequestHandler, TSecAuthService}
import tsec.cipher.symmetric.jca.AES128GCM
import tsec.authentication._

object NormalUserRoutes {

  def routes[F[_]: Sync](
      userService: UserService[F],
      securedRequestHandler: SecuredRequestHandler[
        F,
        UserId,
        User,
        AuthEncryptedCookie[AES128GCM, UserId],
      ],
  ): HttpRoutes[F] = Router(
    "normal-users" -> (signUp(userService) <+> update(userService, securedRequestHandler)),
  )

  def signUp[F[_]: Sync](
      userService: UserService[F],
  ): HttpRoutes[F] = {
    implicit val signUpRequestDecoder: EntityDecoder[F, SignUpRequest] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] { case req @ POST -> Root =>
      for {
        signUpRequest <- req.as[SignUpRequest]
        normalUser <- userService.signUp(signUpRequest)
        result <- Created(normalUser.id.asJson)
      } yield result
    }
  }

  def update[F[_]: Sync](
      userService: UserService[F],
      securedRequestHandler: SecuredRequestHandler[
        F,
        UserId,
        User,
        AuthEncryptedCookie[AES128GCM, UserId],
      ],
  ): HttpRoutes[F] = {
    implicit val userProfileDecoder: EntityDecoder[F, UserProfile] = jsonOf
    val dsl = Http4sDsl[F]
    import dsl._

    securedRequestHandler.liftService(TSecAuthService {
      case req @ PUT -> Root / UUIDVar(id) asAuthed user =>
        for {
          profile <- req.request.as[UserProfile]
          _ <- userService.updateUserProfile(id, profile)
          result <- Ok(user.id.asJson)
        } yield result
    })
  }
}
