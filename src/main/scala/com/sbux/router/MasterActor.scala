package com.sbux.router

import akka.actor.{Actor, ActorRef, Props, Terminated}
import akka.event.Logging
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router, ScatterGatherFirstCompletedRoutingLogic}

import scala.language.postfixOps
import scala.concurrent.duration._

class MasterActor extends Actor {
    val log = Logging.getLogger(context.system, this)
    var router: Router = {
        val list = (1 to 10)
            .view
            .map(i => context.actorOf(Props[WorkerActor], s"worker-$i"))
            .map(actorRef => context watch actorRef)
            .map(actorRef => ActorRefRoutee.apply(actorRef))
            .force
            .toIndexedSeq

        Router(ScatterGatherFirstCompletedRoutingLogic(1 second), list)
    }

    override def receive: Receive = {
        case s: String => {
            log.info("ROUTEES SIZE: " + router.routees.size)
            router.route(s, sender)
        }
        case Terminated(deadActorRef) =>
            log.info("Running terminate")
            router = router.removeRoutee(deadActorRef)
            val replacementActorRef = context.actorOf(Props[WorkerActor])
            context watch replacementActorRef
            router = router.addRoutee(ActorRefRoutee(replacementActorRef))
            log.info("Done terminate")
    }
}
