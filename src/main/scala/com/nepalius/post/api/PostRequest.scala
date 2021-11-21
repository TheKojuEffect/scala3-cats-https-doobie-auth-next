package com.nepalius.post.api



case class PostRequest(
    message: String,
    targetState: String,
    targetZipCode: String,
)
