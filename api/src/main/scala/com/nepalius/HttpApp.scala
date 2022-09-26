package com.nepalius

import cats.*
import cats.effect.Async
import cats.implicits.*
import cats.syntax.all.*
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.Logger

import scala.util.chaining.*

object HttpApp:
  def apply[F[_]: Async](
      first: Controller[F],
      remaining: Controller[F]*,
  ): HttpApp[F] = {
    val allRoutes: HttpRoutes[F] =
      (first +: remaining)
        .map(_.routes)
        .reduceLeft(_ <+> _)
    Router(
      "api" -> allRoutes,
    ).orNotFound
      .pipe(Logger.httpApp(logHeaders = true, logBody = false))
  }
