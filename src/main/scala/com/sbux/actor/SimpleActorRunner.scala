package com.sbux.actor

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import com.sbux.entities.Employee

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

object SimpleActorRunner {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("MyActorSystem")
    import actorSystem.dispatcher
    val props = Props[SimpleActor]
    val actorRef = actorSystem.actorOf(props, "rich")
    actorRef ! "Hello"
    actorRef ! Employee("Hema", "Thirumazhisai")
    actorRef ! BigInt(40402394)

    actorSystem.scheduler
      .schedule(0 milliseconds, 2 seconds, actorRef, "Tick")

    println("Press Return to Stop")
    scala.io.StdIn.readLine()

    val future = actorSystem.terminate()
    Await.ready(future, Duration.apply("10 seconds"))
  }
}
