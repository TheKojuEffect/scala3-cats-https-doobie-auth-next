package dev.koju.locals.auth

import java.util.UUID

import cats.MonadError
import cats.effect.Sync
import cats.implicits._
import dev.koju.locals.user.domain.User.UserId
import dev.koju.locals.user.domain.{Role, User}
import org.http4s.Response
import tsec.authentication.{AuthEncryptedCookie, EncryptedCookieAuthenticator, IdentityStore, SecuredRequest, SecuredRequestHandler, TSecAuthService, TSecCookieSettings}
import tsec.authorization.BasicRBAC
import tsec.cipher.symmetric.IvGen
import tsec.cipher.symmetric.jca.{AES128GCM, JAuthEncryptor}

import scala.concurrent.duration.DurationInt

object Auth {

  def securedRequestHandler[F[_]: Sync](
      identityStore: IdentityStore[F, UserId, User],
  ): SecuredRequestHandler[F, UserId, User, AuthEncryptedCookie[AES128GCM, UserId]] = {

    implicit val encryptor: JAuthEncryptor[F, AES128GCM] = AES128GCM.genEncryptor[F]
    implicit val gcmstrategy: IvGen[F, AES128GCM] = AES128GCM.defaultIvStrategy[F]

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

  private def _adminOnly[F[_], Auth](implicit
      F: MonadError[F, Throwable],
  ): BasicRBAC[F, Role, User, Auth] =
    BasicRBAC[F, Role, User, Auth](Role.Admin)

  def adminOnly[F[_], Auth](
      pf: PartialFunction[SecuredRequest[F, User, AuthEncryptedCookie[Auth, UUID]], F[Response[F]]],
  )(implicit
      F: MonadError[F, Throwable],
  ): TSecAuthService[User, AuthEncryptedCookie[Auth, UUID], F] =
    TSecAuthService.withAuthorization(_adminOnly[F, AuthEncryptedCookie[Auth, UUID]])(pf)
}
