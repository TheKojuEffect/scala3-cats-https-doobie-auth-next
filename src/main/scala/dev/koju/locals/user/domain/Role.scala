package dev.koju.locals.user.domain

import cats.Eq

final case class Role(role: String)

object Role {
  val Admin: Role  = Role("Admin")
  val Normal: Role = Role("Normal")

  implicit val eqRole: Eq[Role] = Eq.fromUniversalEquals[Role]
}
