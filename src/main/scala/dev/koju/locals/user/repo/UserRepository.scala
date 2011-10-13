package dev.koju.locals.user.repo

import cats.effect.Bracket
import cats.implicits._
import dev.koju.locals.user.domain.{NormalUser, UserRepo}
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.{Transactor, Update0}

class UserRepository[F[_]: Bracket[*[_], Throwable]](val transactor: Transactor[F])
    extends UserRepo[F] {

  import UserSql._

  override def create(user: NormalUser): F[NormalUser] =
    insertNormal(user).run
      .transact(transactor)
      .as(user)
}

object UserRepository {
  def apply[F[_]: Bracket[*[_], Throwable]](transactor: Transactor[F]): UserRepository[F] =
    new UserRepository[F](transactor)
}

private object UserSql {

  def insertNormal(user: NormalUser): Update0 =
    sql"""
    INSERT INTO users (id, email, password, role)
    VALUES (${user.id}, ${user.email}, ${user.password}, ${user.role});
    
    INSERT INTO user_profile (user_id, first_name, last_name, city, state, ethnic_country)
    VALUES (${user.id}, ${user.profile.firstName}, ${user.profile.lastName}, ${user.profile.city}, ${user.profile.state}, ${user.profile.ethnicCountry});
  """.update
}
