package com.sbux.remoting

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import com.sbux.router.WorkerActor

import scala.concurrent.Await

object RemoteServerRunner extends App {
    val actorSystem = ActorSystem("RemoteServer-Example-System") //Thread pool for scheduler
    val routerRef =
        actorSystem
            .actorOf(Props[WorkerActor], "workerActor")
    import scala.concurrent.duration._

    routerRef ! "Foo"
    println(routerRef.path.address)
    println("Press RETURN to stop")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
}
