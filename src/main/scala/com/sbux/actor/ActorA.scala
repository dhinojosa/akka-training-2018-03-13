package com.sbux.actor

import akka.actor.Actor
import akka.event.Logging

class ActorA extends Actor{
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Hello" => sender() ! "Hello to you"
    case "How are you doing?" => sender() ! "Fine"
    case _ => log.info("Nothing else to say")
  }
}
