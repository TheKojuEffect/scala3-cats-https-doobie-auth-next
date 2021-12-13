package com.nepalius.post.domain

import com.nepalius.location.State

case class PostRequest(
    message: String,
    targetState: State,
    targetZipCode: String,
)
