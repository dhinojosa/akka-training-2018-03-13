package com.sbux.actor

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import com.sbux.entities.Employee

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object SimpleActorRunner  {
   def main(args:Array[String]):Unit = {
      val actorSystem = ActorSystem("MyActorSystem")
      val props = Props[SimpleActor]
      val actorRef = actorSystem.actorOf(props, "rich")
      actorRef ! "Hello"
      actorRef ! Employee("Hema", "Thirumazhisai")
      actorRef ! BigInt(40402394)

      val future = actorSystem.terminate()
      Await.ready(future, Duration.apply("10 seconds"))
   }
}
