package com.sbux.actor

import akka.actor.{Actor, ActorRef}
import akka.event.Logging

class ActorB extends Actor {
  val log = Logging.apply(context.system, this)

  override def receive: Receive = {
    case a:ActorRef =>
      log.info("Received Actor Ref")
      a ! "Hello"
    case "Hello to you" =>
      sender() ! "How are you doing?"
    case x =>
      log.info("Got else {}", x)
  }
}
