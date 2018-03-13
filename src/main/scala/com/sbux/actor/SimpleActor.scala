package com.sbux.actor

import akka.actor.Actor
import akka.event.Logging
import com.sbux.entities.Employee

class SimpleActor extends Actor {
  val log = Logging.getLogger(context.system, this)

  override def receive: Receive = {
    case x:String => log.info("We got a String called: {}", x)
    case Employee(fn, ln) => log.info("We got an Employee {} {}", fn, ln)
    case y => unhandled(y)
  }
}

