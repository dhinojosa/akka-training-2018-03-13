package com.sbux.actor

import akka.actor.Actor
import akka.event.Logging
import com.typesafe.config.{Config, ConfigFactory}

class ActorA extends Actor{
  val log = Logging(context.system, this)
  val config: Config = ConfigFactory.load()
  val myConfig: Config = config.getConfig("danno").withFallback(config)



  override def preStart(): Unit = {
    log.debug("The actor is about to start")
    super.preStart()
  }

  override def aroundPostStop(): Unit = {
    log.debug("The actor is about to stop")
    super.aroundPostStop()
    log.debug("The actor has stopped")
  }

  override def receive: Receive = {
    case "Hello" => sender() ! "Hello to you"
    case "How are you doing?" => sender() ! "Fine"
    case "Read value" => sender() ! myConfig.getString("akka.loglevel")
    case _ => log.info("Nothing else to say")
  }
}
