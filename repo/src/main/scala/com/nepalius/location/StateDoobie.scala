package com.nepalius.location

import doobie.util.meta.Meta
import doobie.*
import doobie.implicits.*

object StateDoobie:

  implicit val stateMeta: Meta[State] =
    Meta[String].timap(State.valueOf)(_.toString)
