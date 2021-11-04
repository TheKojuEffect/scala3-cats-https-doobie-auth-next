package com.nepalius.user.repo

import cats.data.OptionT
import cats.effect.MonadCancel
import cats.implicits._
import com.nepalius.user.domain.User.UserId
import com.nepalius.user.domain.{NormalUser, User, UserProfile, UserRepo}
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.query.Query0
import doobie.{Transactor, Update0}
import tsec.authentication.IdentityStore

class UserRepoImpl[F[_]: MonadCancel[*[_], Throwable]](val transactor: Transactor[F])
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

  override def updateUserProfile(userId: UserId, profile: UserProfile): F[UserProfile] =
    updateProfile(userId, profile).run
      .transact(transactor)
      .as(profile)
}

object UserRepoImpl {
  def apply[F[_]: MonadCancel[*[_], Throwable]](transactor: Transactor[F]): UserRepoImpl[F] =
    new UserRepoImpl[F](transactor)
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
    
    INSERT INTO user_profile (user_id, first_name, last_name, state, zip_code)
    VALUES (${user.id}, ${user.profile.firstName}, ${user.profile.lastName}, ${user.profile.state}, ${user.profile.zipCode});
  """.update

  def updateProfile(userId: UserId, profile: UserProfile): Update0 =
    sql"""
        UPDATE user_profile
        SET
          first_name = ${profile.firstName},
          last_name = ${profile.lastName},
          state = ${profile.state}
        WHERE user_id = $userId
       """.update
}
