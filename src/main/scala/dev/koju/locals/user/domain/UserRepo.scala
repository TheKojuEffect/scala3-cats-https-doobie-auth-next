package dev.koju.locals.user.domain

import cats.data.OptionT
import dev.koju.locals.user.domain.User.UserId

trait UserRepo[F[_]] {

  def create(user: NormalUser): F[NormalUser]

  def getUserByEmail(email: String): OptionT[F, User]

  def getUser(id: UserId): OptionT[F, User]
}
