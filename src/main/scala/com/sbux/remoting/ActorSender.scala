package com.sbux.remoting

import akka.actor.Actor
import akka.event.Logging

class ActorSender extends Actor {
    val log = Logging.getLogger(context.system, this)

    override def receive: Receive = {
        case s:String =>
            val selection = context.actorSelection(
                "akka.tcp://RemoteServer-Example-System@127.0.0.1:2552/user/workerActor")
            selection ! ("Sending " + s)
            log.info("Got string {}", s)

        case x => unhandled(x)
    }
}
