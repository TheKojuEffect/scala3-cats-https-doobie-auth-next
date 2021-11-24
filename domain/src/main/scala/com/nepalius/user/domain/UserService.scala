package com.nepalius.user.domain

import cats.FlatMap
import cats.data.OptionT
import cats.implicits.*
import User.UserId

trait UserService[F[_]]:
  def getUser(id: UserId): OptionT[F, User]
  def getUserByEmail(email: String): OptionT[F, User]
  def create(normalUser: NormalUser): F[NormalUser]
  def updateUserProfile(userId: UserId, profile: UserProfile): F[UserProfile]

object UserService:

  def apply[F[_]: FlatMap](
      userRepo: UserRepo[F],
  ): UserService[F] = new UserService[F] {

    override def getUser(id: UserId): OptionT[F, User] = userRepo.getUser(id)

    override def getUserByEmail(email: String): OptionT[F, User] =
      userRepo.getUserByEmail(email)

    override def create(normalUser: NormalUser): F[NormalUser] =
      userRepo.create(normalUser)

    override def updateUserProfile(
        userId: UserId,
        profile: UserProfile,
    ): F[UserProfile] =
      userRepo.updateUserProfile(userId, profile)
  }
