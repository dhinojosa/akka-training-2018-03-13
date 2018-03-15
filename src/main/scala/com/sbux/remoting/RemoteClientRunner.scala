package com.sbux.remoting

import akka.actor.{ActorRef, ActorSystem, Cancellable, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.language.postfixOps

object RemoteClientRunner extends App {

  private val mainConfig = ConfigFactory.load

  private val modConfig = mainConfig
    .getConfig("client")
    .withFallback(mainConfig)

  println("port=" + modConfig.getInt("akka.remote.netty.tcp.port"))

  val actorSystem = ActorSystem("Remote-Client-Example-System", modConfig)

  import actorSystem.dispatcher
  import scala.concurrent.duration._

  //1. Ask your name what their ActorSystem name is
  private val ref: ActorRef = actorSystem.actorOf(Props[ActorSender])
  ref ! "Foo"

  println("Press RETURN to stop")
  scala.io.StdIn.readLine()
  Await.ready(actorSystem.terminate(), 10 seconds)
}
