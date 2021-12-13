package com.nepalius.effect

import java.util.UUID
import cats.effect.Sync
import cats.ApplicativeThrow

trait UuidGen[F[_]]:
  def make: F[UUID]
  def read(string: String): F[UUID]

object UuidGen:
  def apply[F[_]: UuidGen]: UuidGen[F] = implicitly

  implicit def forSync[F[_]: Sync]: UuidGen[F] =
    new UuidGen[F]:
      def make: F[UUID] = Sync[F].delay(UUID.randomUUID)
      def read(string: String): F[UUID] =
        ApplicativeThrow[F].catchNonFatal(UUID.fromString(string))
