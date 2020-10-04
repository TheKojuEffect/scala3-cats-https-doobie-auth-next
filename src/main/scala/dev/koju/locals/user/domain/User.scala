package dev.koju.locals.user.domain

import java.util.UUID

import dev.koju.locals.user.domain.User.UserId

case class User(
    id: UserId,
    email: String,
    password: String,
    role: Role,
)
object User {
  type UserId = UUID
}

final case class NormalUser(
    id: UserId,
    email: String,
    password: String,
    profile: UserProfile,
)

final case class AdminUser(
    id: UserId,
    email: String,
    password: String,
)
