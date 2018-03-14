package com.sbux.router

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Cancellable, Props, Scheduler}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

object RouterRunner extends App {
    val actorSystem = ActorSystem("Router1-Example-System")
    import actorSystem.dispatcher //Thread pool for scheduler
    val masterActorRef = actorSystem.actorOf(Props[MasterActor])

    private val scheduler: Scheduler = actorSystem.scheduler
    private val cancellable: Cancellable = scheduler.schedule(
        0 milliseconds,
        50 milliseconds){

        val i = Random.nextInt(10)
        if (i == 5) masterActorRef ! "terminate"
        else masterActorRef ! LocalDateTime.now().toString
    }

    println("Press Enter to Terminate")
    scala.io.StdIn.readLine()
    cancellable.cancel()
    Thread.sleep(5000)
    Await.ready(actorSystem.terminate(), 10 seconds)
}
