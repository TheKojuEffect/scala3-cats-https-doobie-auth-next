package com.nepalius.post.domain

import com.nepalius.user.domain.User
import cats.implicits.*

import java.time.LocalDateTime
import java.util.UUID
import com.nepalius.effect.UuidGen
import cats.effect.Sync
import cats.effect.std.Random
import cats.effect.kernel.Clock
import io.chrisdavenport.cats.effect.time.implicits.*

trait PostService[F[_]]:
  def create(post: PostRequest, user: User): F[Post]

object PostService:
  def apply[F[_]: Sync: Clock](postRepo: PostRepo[F]): PostService[F] =
    new PostService[F] {
      override def create(post: PostRequest, user: User): F[Post] =
        for
          id <- UuidGen[F].make
          now <- Clock[F].getLocalDateTimeUTC
          p = Post(
            id,
            post.message,
            post.targetState,
            post.targetZipCode,
            user.id,
            now,
          )
          created <- postRepo.create(p)
        yield created
    }
