package com.nepalius.user.domain
import enumeratum._

import scala.annotation.nowarn

sealed abstract class State(@nowarn("cat=unused-params") name: String) extends EnumEntry

object State extends Enum[State] with DoobieEnum[State] with CirceEnum[State] {

  val values: IndexedSeq[State] = findValues

  case object AL extends State("Alabama")
  case object AK extends State("Alaska")
  case object AZ extends State("Arizona")
  case object AR extends State("Arkansas")
  case object CA extends State("California")
  case object CO extends State("Colorado")
  case object CT extends State("Connecticut")
  case object DE extends State("Delaware")
  case object DC extends State("District of Columbia")
  case object FL extends State("Florida")
  case object GA extends State("Georgia")
  case object HI extends State("Hawaii")
  case object ID extends State("Idaho")
  case object IL extends State("Illinois")
  case object IN extends State("Indiana")
  case object IA extends State("Iowa")
  case object KS extends State("Kansas")
  case object KY extends State("Kentucky")
  case object LA extends State("Louisiana")
  case object ME extends State("Maine")
  case object MD extends State("Maryland")
  case object MA extends State("Massachusetts")
  case object MI extends State("Michigan")
  case object MN extends State("Minnesota")
  case object MS extends State("Mississippi")
  case object MO extends State("Missouri")
  case object MT extends State("Montana")
  case object NE extends State("Nebraska")
  case object NV extends State("Nevada")
  case object NH extends State("New Hampshire")
  case object NJ extends State("New Jersey")
  case object NM extends State("New Mexico")
  case object NY extends State("New York")
  case object NC extends State("North Carolina")
  case object ND extends State("North Dakota")
  case object OH extends State("Ohio")
  case object OK extends State("Oklahoma")
  case object OR extends State("Oregon")
  case object PA extends State("Pennsylvania")
  case object RI extends State("Rhode Island")
  case object SC extends State("South Carolina")
  case object SD extends State("South Dakota")
  case object TN extends State("Tennessee")
  case object TX extends State("Texas")
  case object UT extends State("Utah")
  case object VT extends State("Vermont")
  case object VA extends State("Virginia")
  case object WA extends State("Washington")
  case object WV extends State("West Virginia")
  case object WI extends State("Wisconsin")
  case object WY extends State("Wyoming")
}
