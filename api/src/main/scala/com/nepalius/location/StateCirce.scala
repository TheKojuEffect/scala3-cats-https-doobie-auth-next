package com.nepalius.location
import scala.util.Try
import io.circe.*
import io.circe.generic.semiauto.*

object StateCirce:

  given Encoder[State] =
    Encoder.encodeString.contramap[State](_.toString)

  given Decoder[State] = Decoder.decodeString.emapTry { str =>
    Try(State.valueOf(str))
  }
