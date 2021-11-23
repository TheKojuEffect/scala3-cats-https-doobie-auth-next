package com.nepalius

import cats.effect.Resource
import cats.effect.kernel.Async
import cats.implicits.*
import com.nepalius.auth.Auth
import com.nepalius.auth.api.AuthRoutes
import com.nepalius.config.{AppConfig, DatabaseSetup}
import com.nepalius.post.api.PostRoutes
import com.nepalius.post.domain.PostService
import com.nepalius.post.repo.PostRepoImpl
import com.nepalius.user.api.NormalUserRoutes
import com.nepalius.user.domain.UserService
import com.nepalius.user.repo.UserRepoImpl
import com.nepalius.view.ViewRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Server as HttpServer
import tsec.passwordhashers.jca.BCrypt

object Server:
  def create[F[_]: Async]: Resource[F, HttpServer] =
    for
      conf <- Resource.pure(AppConfig.default)
      transactor <- DatabaseSetup.dbTransactor(conf.db)
      userRepo = UserRepoImpl(transactor)
      postRepo = PostRepoImpl(transactor)
      passwordHasher = BCrypt.syncPasswordHasher[F]
      userService = UserService(userRepo)
      authHandler = Auth.authHandler(userService)
      postService = PostService(postRepo)
      httpApp = (
        ViewRoutes.index <+>
          AuthRoutes.routes(userService, passwordHasher, authHandler) <+>
          NormalUserRoutes.routes(userService, authHandler, passwordHasher) <+>
          PostRoutes.routes(authHandler, postService)
      ).orNotFound
      _ <- Resource.eval(DatabaseSetup.initDb(conf.db))
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    yield server
