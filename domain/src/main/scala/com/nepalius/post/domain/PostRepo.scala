package com.nepalius.post.domain

trait PostRepo[F[_]]:
  def create(post: Post): F[Post]
