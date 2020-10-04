package dev.koju.locals.user.repo

import cats.Applicative
import cats.implicits._
import dev.koju.locals.user.domain.{NormalUser, UserRepo}

class UserRepository[F[_]: Applicative] extends UserRepo[F] {
  override def create(user: NormalUser): F[NormalUser] = user.pure[F]
}

object UserRepository {
  def apply[F[_]: Applicative]() = new UserRepository[F]()
}
