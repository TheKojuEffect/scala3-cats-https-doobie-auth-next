package com.nepalius.auth

import cats.effect.Sync
import cats.implicits.*
import com.nepalius.user.domain.User
import com.nepalius.user.domain.UserService
import User.UserId
import tsec.authentication.*

import tsec.cipher.symmetric.IvGen
import tsec.cipher.symmetric.jca.{AES128GCM, JAuthEncryptor}

import scala.concurrent.duration.DurationInt

object Auth:

  type AuthHandler[F[_]] = SecuredRequestHandler[
    F,
    UserId,
    User,
    AuthEncryptedCookie[AES128GCM, UserId],
  ]

  def authHandler[F[_]: Sync](
      userService: UserService[F],
  ): AuthHandler[F] =

    implicit val encryptor: JAuthEncryptor[F, AES128GCM] =
      AES128GCM.genEncryptor[F]
    implicit val gcmStrategy: IvGen[F, AES128GCM] =
      AES128GCM.defaultIvStrategy[F]

    val identityStore = new IdentityStore[F, UserId, User] {
      override def get(id: UserId) = userService.getUser(id)
    }

    val key = AES128GCM.generateKey
    val settings = TSecCookieSettings(
      cookieName = "auth",
      secure = false,
      expiryDuration = 10.minutes,
      maxIdle = None,
    )
    val statelessAuthenticator = EncryptedCookieAuthenticator.stateless(
      settings,
      identityStore,
      key,
    )
    SecuredRequestHandler(statelessAuthenticator)
