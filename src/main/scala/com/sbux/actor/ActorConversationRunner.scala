package com.sbux.actor

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ActorConversationRunner extends App {
  //ActorSystem, call it what you want
  //Use actorOf twice to create ActorA, actorB
  //Send B the message "begin"
  //Terminate the system

  private val system = ActorSystem("My-Actor-System")
  val actorA: ActorRef = system.actorOf(Props[ActorA], "actorA")
  val actorB: ActorRef = system.actorOf(Props[ActorB], "actorB")
  actorB ! actorA

  println("Press Enter to Terminate System")
  scala.io.StdIn.readLine()

  Await.ready(system.terminate(), Duration.apply(10, TimeUnit.SECONDS))
}
