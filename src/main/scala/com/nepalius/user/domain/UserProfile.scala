package com.nepalius.user.domain

import com.nepalius.location.domain.State

case class UserProfile(
    firstName: String,
    lastName: String,
    state: State,
    zipCode: String,
)
