package com.nepalius.user.domain

import cats._
import cats.implicits._

import tsec.authorization.{AuthGroup, SimpleAuthEnum}

final case class Role(role: String)

object Role extends SimpleAuthEnum[Role, String] {
  val Admin: Role = Role("Admin")
  val Normal: Role = Role("Normal")

  override val values: AuthGroup[Role] = AuthGroup(Admin, Normal)

  override def getRepr(r: Role): String = r.role

  implicit val eqRole: Eq[Role] = Eq.fromUniversalEquals[Role]
}
