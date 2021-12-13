package com.nepalius.location

import doobie.util.meta.Meta
import doobie.*
import doobie.implicits.*

object StateDoobie:

  given Meta[State] = Meta[String].timap(State.valueOf)(_.toString)
