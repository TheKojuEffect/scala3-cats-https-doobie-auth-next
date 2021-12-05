package com.nepalius.location
import scala.util.Try
import io.circe.*
import io.circe.generic.semiauto.*

object StateCirce:

  implicit val stateEncoder: Encoder[State] =
    Encoder.encodeString.contramap[State](_.toString)

  implicit val stateDecoder: Decoder[State] = Decoder.decodeString.emapTry {
    str => Try(State.valueOf(str))
  }
