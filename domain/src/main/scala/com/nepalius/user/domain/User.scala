package com.nepalius.user.domain

import cats.Applicative
import com.nepalius.user.domain.User.UserId

import java.util.UUID

case class User(
    id: UserId,
    email: String,
    password: String,
    role: Role,
)
object User:
  type UserId = UUID

final case class NormalUser(
    id: UUID,
    email: String,
    password: String,
    profile: UserProfile,
):
  def role: Role = Role.Normal

final case class AdminUser(
    id: UUID,
    email: String,
    password: String,
):
  def role: Role = Role.Admin
