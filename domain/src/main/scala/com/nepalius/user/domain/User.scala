package com.nepalius.user.domain

import java.util.UUID
import cats.Applicative
import User.UserId
import io.circe.Encoder
import tsec.authorization.AuthorizationInfo

case class User(
    id: UserId,
    email: String,
    password: String,
    role: Role,
)
object User:
  type UserId = UUID

  implicit def authRole[F[_]](implicit F: Applicative[F]): AuthorizationInfo[F, Role, User] =
    (u: User) => F.pure(u.role)

  implicit val userEncoder: Encoder[User] = Encoder.forProduct3("id", "email", "role")(u => (u.id, u.email, u.role))

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
