package dev.koju.locals.user.domain

import cats.FlatMap
import cats.implicits._
import tsec.passwordhashers.PasswordHasher

trait UserService[F[_]] {
  def signUp(request: SignUpRequest): F[NormalUser]
}

object UserService {

  def apply[F[_]: FlatMap, A](
      userRepo: UserRepo[F],
      passwordHasher: PasswordHasher[F, A],
  ): UserService[F] =
    (request: SignUpRequest) =>
      for {
        passwordHash <- passwordHasher.hashpw(request.password)
        normalUser = request.asNormalUser(passwordHash)
        savedUser <- userRepo.create(normalUser)
      } yield savedUser
}
