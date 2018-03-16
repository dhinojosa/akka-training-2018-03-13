package com.sbux.stream

import java.time.LocalDateTime

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object AkkaStreamRunner extends App {
    implicit val actorSystem = ActorSystem("Akka-Stream-System")
    implicit val materializer = ActorMaterializer()
    import actorSystem.dispatcher


    //Source
    //Flow
    //Sink


    private val mapWithThread = Flow[String].map { x => println("0: " + Thread.currentThread().getName); x }

    private val combinedSource: Source[String, Cancellable] = Source
        .tick(0 milliseconds, 10 milliseconds, "Ping")
        .via(mapWithThread)
        .async

    private val sink = Sink.foreach(println)

    private val value: RunnableGraph[(Cancellable, Future[Done])] =
        combinedSource.toMat(sink)(Keep.both)

    val t = value.run()
    t._2.onComplete {
        case Success(_) => println("Done")
        case Failure(_) => println("Fail")
    }

    Thread.sleep(2000)
    t._1.cancel()


    println("Press RETURN to stop")
    scala.io.StdIn.readLine()
    Await.ready(actorSystem.terminate(), 10 seconds)
}
