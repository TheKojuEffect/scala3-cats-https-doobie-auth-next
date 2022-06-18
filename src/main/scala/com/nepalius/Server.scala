package com.nepalius

import cats.effect.Resource
import cats.effect.kernel.Async
import cats.implicits.*
import com.comcast.ip4s.*
import com.nepalius.auth.Auth
import com.nepalius.auth.api.AuthController
import com.nepalius.config.{AppConfig, DatabaseSetup}
import com.nepalius.post.api.PostController
import com.nepalius.post.domain.PostService
import com.nepalius.post.repo.PostRepoImpl
import com.nepalius.user.api.NormalUserController
import com.nepalius.user.domain.UserService
import com.nepalius.user.repo.UserRepoImpl
import io.circe.config.parser
import io.circe.generic.auto.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Server as HttpServer
import tsec.passwordhashers.jca.BCrypt

object Server:
  def create[F[_]: Async]: Resource[F, HttpServer] =
    for
      conf <- Resource.eval(parser.decodePathF[F, AppConfig]("nepalius"))
      transactor <- DatabaseSetup.dbTransactor(conf.db)
      userRepo = UserRepoImpl(transactor)
      postRepo = PostRepoImpl(transactor)
      passwordHasher = BCrypt.syncPasswordHasher[F]
      userService = UserService(userRepo)
      authHandler = Auth.authHandler(userService)
      postService = PostService(postRepo)
      httpApp = HttpApp(
        AuthController(userService, passwordHasher, authHandler),
        NormalUserController(userService, authHandler, passwordHasher),
        PostController(authHandler, postService),
      )
      _ <- Resource.eval(DatabaseSetup.initDb(conf.db))
      server <- EmberServerBuilder
        .default[F]
        .withHost(Host.fromString(conf.server.host).get)
        .withPort(Port.fromInt(conf.server.port).get)
        .withHttpApp(httpApp)
        .build
    yield server
