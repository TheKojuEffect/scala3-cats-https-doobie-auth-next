package com.nepalius.user.domain

import java.util.UUID

final case class SignUpRequest(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    city: String,
    state: String,
    ethnicCountry: String,
) {
  def asNormalUser[A](passwordHash: String): NormalUser =
    NormalUser(
      UUID.randomUUID(),
      email,
      passwordHash,
      UserProfile(firstName, lastName, city, state, ethnicCountry),
    )
}
