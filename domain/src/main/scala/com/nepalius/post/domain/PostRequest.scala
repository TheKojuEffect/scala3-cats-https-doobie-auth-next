package com.nepalius.post.domain

case class PostRequest(
    message: String,
    targetState: String,
    targetZipCode: String,
)
