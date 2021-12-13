package com.nepalius.user.domain

import com.nepalius.location.State

import java.util.UUID
import com.nepalius.effect.UuidGen
import cats.implicits.*
import cats.Functor

final case class SignUpRequest(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    state: State,
    zipCode: String,
):
  def asNormalUser[F[_]: UuidGen: Functor, A](
      passwordHash: String,
  ): F[NormalUser] =
    for
      id <- UuidGen[F].make
      normalUser = NormalUser(
        id,
        email,
        passwordHash,
        UserProfile(firstName, lastName, state, zipCode),
      )
    yield normalUser
