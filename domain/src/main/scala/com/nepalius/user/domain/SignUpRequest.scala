package com.nepalius.user.domain

import com.nepalius.location.State

import java.util.UUID

final case class SignUpRequest(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    state: State,
    zipCode: String,
):
  def asNormalUser[A](passwordHash: String): NormalUser =
    NormalUser(
      UUID.randomUUID(),
      email,
      passwordHash,
      UserProfile(firstName, lastName, state, zipCode),
    )
