package dev.koju.locals.user.api

import cats.effect.Sync
import cats.implicits._
import dev.koju.locals.user.domain.{SignUpRequest, UserService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{EntityDecoder, HttpRoutes}

object NormalUserRoutes {

  def routes[F[_]: Sync](userService: UserService[F]): HttpRoutes[F] = Router(
    "normal-users" -> signUp(userService),
  )

  def signUp[F[_]: Sync](userService: UserService[F]): HttpRoutes[F] = {
    implicit val signUpRequestDecoder: EntityDecoder[F, SignUpRequest] = jsonOf
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        for {
          signUpRequest <- req.as[SignUpRequest]
          normalUser <- userService.signUp(signUpRequest)
          result <- Created(normalUser.id.asJson)
        } yield result
    }
  }
}
