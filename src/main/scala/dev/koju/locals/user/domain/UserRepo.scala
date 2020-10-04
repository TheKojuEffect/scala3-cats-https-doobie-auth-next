package dev.koju.locals.user.domain

trait UserRepo[F[_]] {
  def create(user: NormalUser): F[NormalUser]
}
