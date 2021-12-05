package com.nepalius.user.domain

import com.nepalius.location.State

case class UserProfile(
    firstName: String,
    lastName: String,
    state: State,
    zipCode: String,
)
