package com.sbux.faulttolerance

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, AllForOneStrategy, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration.Duration
import scala.util.Random

class ParentActor extends Actor {

  override def supervisorStrategy: SupervisorStrategy =
    AllForOneStrategy(3, Duration("1 second"), true) {
      case x:IllegalArgumentException => Restart
      case _ => Escalate
    }

  override def receive: Receive = {
    case p:Props =>
      val listOfChildren = (1 to 10).map(x => context.actorOf(p, "child" + x))
      sender ! listOfChildren(Random.nextInt(10))
  }
}
