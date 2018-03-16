package com.sbux.clusterclient

import akka.actor.{ActorPath, ActorRef, ActorSystem}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}

import scala.concurrent.Await
import scala.io.StdIn
import scala.concurrent.duration._

object ClusterClientRunner extends App {

    val actorSystem = ActorSystem("Non-Member-Actor-System")
    val initialContacts = Set(
        ActorPath.fromString("akka.tcp://My-Cluster@52.183.118.93:2552/system/receptionist"),
        ActorPath.fromString("akka.tcp://My-Cluster@52.183.118.93:2551/system/receptionist"))

    val c: ActorRef = actorSystem.actorOf(
        ClusterClient.props(ClusterClientSettings(actorSystem)
            .withInitialContacts(initialContacts)))
    c ! ClusterClient.Send("/user/simpleRouter", "Hello from Danno!", false)

    println("Press ENTER to continue")
    StdIn.readLine()

    Await.ready(actorSystem.terminate(), 10 seconds)


}
