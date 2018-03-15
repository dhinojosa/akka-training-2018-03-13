package com.sbux.router

import akka.actor.{ActorSystem, Cancellable, Props}
import akka.routing.FromConfig

import scala.concurrent.Await
import scala.util.Random

object PoolRouterRunner extends App {
    val actorSystem = ActorSystem("PoolRouter-Example-System")
    import actorSystem.dispatcher //Thread pool for scheduler
    val routerRef =
        actorSystem
            .actorOf(FromConfig.props(Props[WorkerActor]),
                "workerActor")
    import scala.concurrent.duration._


    private val cancellable: Cancellable = actorSystem
        .scheduler
        .schedule(
            0 milliseconds,
            500 milliseconds) {

            if (Random.nextInt(10) == 5) routerRef ! "terminate"
            else routerRef ! "Hello"
        }

    println("Press RETURN to stop")
    scala.io.StdIn.readLine()
    cancellable.cancel()
    Await.ready(actorSystem.terminate(), 10 seconds)
}
