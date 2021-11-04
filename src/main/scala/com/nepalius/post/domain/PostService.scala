package com.nepalius.post.domain

import com.nepalius.location.domain.State
import com.nepalius.post.api.PostRequest
import com.nepalius.user.domain.User

import java.time.LocalDateTime
import java.util.UUID

trait PostService[F[_]] {
  def create(post: PostRequest, user: User): F[Post]
}

object PostService {
  def apply[F[_]](postRepo: PostRepo[F]): PostService[F] = new PostService[F] {
    override def create(post: PostRequest, user: User): F[Post] =
      postRepo.create(Post(UUID.randomUUID(), post.message, State.VA, "3232", user.id, LocalDateTime.now()))
  }
}
