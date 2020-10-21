package dev.koju.locals.user.repo

import cats.data.OptionT
import cats.effect.Bracket
import cats.implicits._
import dev.koju.locals.user.domain.User.UserId
import dev.koju.locals.user.domain.{NormalUser, User, UserRepo}
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.query.Query0
import doobie.{Transactor, Update0}
import tsec.authentication.IdentityStore

class UserRepository[F[_]: Bracket[*[_], Throwable]](val transactor: Transactor[F])
    extends UserRepo[F]
    with IdentityStore[F, UserId, User] {

  import UserSql._

  override def get(id: UserId): OptionT[F, User] = getUser(id)

  override def getUser(id: UserId): OptionT[F, User] =
    OptionT(getById(id).option.transact(transactor))

  override def getUserByEmail(email: String): OptionT[F, User] =
    OptionT(getByEmail(email).option.transact(transactor))

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

  def getById(id: UserId): Query0[User] =
    sql"""
         SELECT id, email, password, role
         FROM users
         WHERE id = ${id}
       """.query[User]

  def getByEmail(email: String): Query0[User] =
    sql"""
         SELECT id, email, password, role
         FROM users
         WHERE email = ${email}
       """.query[User]

  def insertNormal(user: NormalUser): Update0 =
    sql"""
    INSERT INTO users (id, email, password, role)
    VALUES (${user.id}, ${user.email}, ${user.password}, ${user.role});
    
    INSERT INTO user_profile (user_id, first_name, last_name, city, state, ethnic_country)
    VALUES (${user.id}, ${user.profile.firstName}, ${user.profile.lastName}, ${user.profile.city}, ${user.profile.state}, ${user.profile.ethnicCountry});
  """.update
}
