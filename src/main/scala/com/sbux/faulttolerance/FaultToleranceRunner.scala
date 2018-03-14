package com.sbux.faulttolerance

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object FaultToleranceRunner extends App {
  val system = ActorSystem("FaultToleranceSystem")
  import system.dispatcher
  val parentRef = system.actorOf(Props[ParentActor], "parentActor")
  system.actorOf(Props[ErrorActor], "errorActor")

  implicit val timeout: Timeout = Timeout(10L, TimeUnit.SECONDS)
  private val future: Future[Any] = parentRef ? Props[ChildActor]

  future.mapTo[ActorRef].foreach(ar => ar ! "IllegalArgumentException")
  Await.ready(future, Duration("10 seconds")) //Blocking

  println("Press RETURN to continue")
  scala.io.StdIn.readLine()

  Await.ready(system.terminate(), Duration("10 seconds"))
}
