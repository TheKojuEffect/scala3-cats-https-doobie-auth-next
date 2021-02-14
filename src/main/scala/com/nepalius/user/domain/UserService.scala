package com.nepalius.user.domain

import cats.FlatMap
import cats.data.OptionT
import cats.implicits._
import User.UserId
import tsec.passwordhashers.PasswordHasher

trait UserService[F[_]] {
  def getUserByEmail(email: String): OptionT[F, User]
  def signUp(request: SignUpRequest): F[NormalUser]
  def updateUserProfile(userId: UserId, profile: UserProfile): F[UserProfile]
}

object UserService {

  def apply[F[_]: FlatMap, A](
      userRepo: UserRepo[F],
      passwordHasher: PasswordHasher[F, A],
  ): UserService[F] = new UserService[F] {

    override def getUserByEmail(email: String): OptionT[F, User] = userRepo.getUserByEmail(email)

    override def signUp(request: SignUpRequest): F[NormalUser] =
      for {
        passwordHash <- passwordHasher.hashpw(request.password)
        normalUser = request.asNormalUser(passwordHash)
        savedUser <- userRepo.create(normalUser)
      } yield savedUser

    override def updateUserProfile(userId: UserId, profile: UserProfile): F[UserProfile] =
      userRepo.updateUserProfile(userId, profile)
  }
}
