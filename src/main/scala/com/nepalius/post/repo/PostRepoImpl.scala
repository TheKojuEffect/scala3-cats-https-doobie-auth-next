package com.nepalius.post.repo

import cats.effect.MonadCancelThrow
import cats.implicits.*
import com.nepalius.post.domain.{Post, PostRepo}
import com.nepalius.post.repo.PostSql.insert
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.{Transactor, Update0}

class PostRepoImpl[F[_]: MonadCancelThrow](val transactor: Transactor[F]) extends PostRepo[F]:
  override def create(post: Post): F[Post] =
    insert(post).run.transact(transactor).as(post)

object PostRepoImpl:
  def apply[F[_]: MonadCancelThrow](transactor: Transactor[F]): PostRepoImpl[F] =
    new PostRepoImpl[F](transactor)

private object PostSql:

  def insert(post: Post): Update0 =
    sql"""
         INSERT INTO post (id, message, target_state, target_zip_code, created_by)
         VALUES (${post.id}, ${post.message}, ${post.targetState}, ${post.targetZipCode}, ${post.createdBy});
       """.update
