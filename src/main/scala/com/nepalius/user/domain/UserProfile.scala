package com.nepalius.user.domain

case class UserProfile(
    firstName: String,
    lastName: String,
    state: State,
    zipCode: String,
)
