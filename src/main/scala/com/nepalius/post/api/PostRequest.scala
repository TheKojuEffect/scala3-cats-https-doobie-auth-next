package com.nepalius.post.api

import com.nepalius.location.domain.State

case class PostRequest(
    message: String,
    targetState: State,
    targetZipCode: String,
)
