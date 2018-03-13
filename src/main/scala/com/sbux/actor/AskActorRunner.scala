package com.sbux.actor

import akka.actor.{ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object AskActorRunner extends App {
  val actorSystem = ActorSystem("My-Actor-System")
  import actorSystem.dispatcher

  implicit val timeout: Timeout = Timeout(10 seconds)

  val actorRef = actorSystem.actorOf(Props[ActorA], "actorA")
  val future: Future[Any] = actorRef ? "Hello"
  future.foreach(println)

  println("Press Enter to terminate")
  scala.io.StdIn.readLine()

  Await.ready(actorSystem.terminate(), 10 seconds)
}
