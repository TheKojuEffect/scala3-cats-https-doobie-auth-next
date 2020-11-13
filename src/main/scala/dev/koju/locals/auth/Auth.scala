package dev.koju.locals.auth

import cats.effect.Sync
import cats.implicits._
import dev.koju.locals.user.domain.User
import dev.koju.locals.user.domain.User.UserId
import tsec.authentication.{AuthEncryptedCookie, EncryptedCookieAuthenticator, IdentityStore, SecuredRequestHandler, TSecCookieSettings}
import tsec.cipher.symmetric.IvGen
import tsec.cipher.symmetric.jca.{AES128GCM, JAuthEncryptor}

import scala.concurrent.duration.DurationInt

object Auth {

  def securedRequestHandler[F[_]: Sync](
      identityStore: IdentityStore[F, UserId, User],
  ): SecuredRequestHandler[F, UserId, User, AuthEncryptedCookie[AES128GCM, UserId]] = {

    implicit val encryptor: JAuthEncryptor[F, AES128GCM] = AES128GCM.genEncryptor[F]
    implicit val gcmStrategy: IvGen[F, AES128GCM] = AES128GCM.defaultIvStrategy[F]

    for {
      key <- AES128GCM.generateKey
      settings = TSecCookieSettings(
        cookieName = "auth",
        secure = false,
        expiryDuration = 10.minutes,
        maxIdle = None,
      )
      statelessAuthenticator =
        EncryptedCookieAuthenticator.stateless(
          settings,
          identityStore,
          key,
        )
    } yield SecuredRequestHandler(statelessAuthenticator)
  }

}
