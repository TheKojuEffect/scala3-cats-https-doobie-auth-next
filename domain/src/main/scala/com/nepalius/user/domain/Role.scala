package com.nepalius.user.domain

import cats.*
import cats.implicits.*

final case class Role(role: String)

object Role:
  val Admin: Role = Role("Admin")
  val Normal: Role = Role("Normal")

  implicit val eqRole: Eq[Role] = Eq.fromUniversalEquals[Role]
