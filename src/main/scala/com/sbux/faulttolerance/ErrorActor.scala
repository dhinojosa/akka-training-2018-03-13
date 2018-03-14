package com.sbux.faulttolerance

import akka.actor.Actor
import akka.event.Logging

class ErrorActor extends Actor {
  val log = Logging.getLogger(context.system, this)
  override def receive: Receive = {
    case s:String =>
      log.error("Something bad happened sending an email: {}", s)
  }
}
