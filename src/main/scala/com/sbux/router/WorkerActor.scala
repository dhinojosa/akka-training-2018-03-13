package com.sbux.router

import akka.actor.Actor
import akka.event.Logging

class WorkerActor extends Actor {
    val log = Logging.getLogger(context.system, this)
    override def receive: Receive = {
        case s:String if s == "terminate" =>
            log.info("So long world")
            context.stop(self)
        case s:String =>
            log.info("Received a String in actor {}: {}", self.path.name, s)
            val i = scala.util.Random.nextInt(20)
            if (i == 5) Thread.sleep(5000)
        case x => unhandled(x)
    }
}
