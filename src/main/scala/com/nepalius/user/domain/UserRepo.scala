package com.nepalius.user.domain

import cats.data.OptionT
import User.UserId

trait UserRepo[F[_]]:

  def create(user: NormalUser): F[NormalUser]

  def getUserByEmail(email: String): OptionT[F, User]

  def getUser(id: UserId): OptionT[F, User]

  def updateUserProfile(userId: UserId, profile: UserProfile): F[UserProfile]
