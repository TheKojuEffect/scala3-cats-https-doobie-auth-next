package com.nepalius.auth

import cats.effect.Sync
import cats.implicits._
import com.nepalius.user.domain.User
import User.UserId
import tsec.authentication._

import tsec.cipher.symmetric.IvGen
import tsec.cipher.symmetric.jca.{AES128GCM, JAuthEncryptor}

import scala.concurrent.duration.DurationInt

object Auth {

  type AuthHandler[F[_]] = SecuredRequestHandler[F, UserId, User, AuthEncryptedCookie[AES128GCM, UserId]]
//
//  def authHandler[F[_]: Sync](
//      identityStore: IdentityStore[F, UserId, User],
//  ): AuthHandler[F] = {
//
//    implicit val encryptor: JAuthEncryptor[F, AES128GCM] = AES128GCM.genEncryptor[F]
//    implicit val gcmStrategy: IvGen[F, AES128GCM] = AES128GCM.defaultIvStrategy[F]
//
//    for {
//      key <- AES128GCM.generateKey
//      settings = TSecCookieSettings(
//        cookieName = "auth",
//        secure = false,
//        expiryDuration = 10.minutes,
//        maxIdle = None,
//      )
//      statelessAuthenticator =
//        EncryptedCookieAuthenticator.stateless(
//          settings,
//          identityStore,
//          key,
//        )
//    } yield SecuredRequestHandler(statelessAuthenticator)
//  }

  def authHandler[F[_]: Sync](
      identityStore: IdentityStore[F, UserId, User],
  ): AuthHandler[F] = ???

}
