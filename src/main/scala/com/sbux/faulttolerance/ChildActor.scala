package com.sbux.faulttolerance

import akka.actor.Actor
import akka.event.Logging

class ChildActor extends Actor {

  val log = Logging.getLogger(context.system, this)

  override def preStart(): Unit = {
    log.info("Child started")
    super.preStart()
  }

  override def postStop(): Unit = {
    log.info("Child stopped")
    super.postStop()
  }

  override def preRestart(reason: Throwable,
                          message: Option[Any]): Unit = {
    context.actorSelection("/user/errorActor") ! reason.getMessage
    log.info("Child restarting because of {}", message)
    super.preRestart(reason, message)
  }


  override def postRestart(reason: Throwable): Unit = {
    log.info("Child restarted because of {}", reason)
    super.postRestart(reason)
  }

  override def receive: Receive = {
    case "IllegalArgumentException" =>
      throw new IllegalArgumentException("Oh no!" + self.path.name)
  }
}
