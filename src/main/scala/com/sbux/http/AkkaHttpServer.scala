package com.sbux.http

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.sbux.actor.SimpleActor

import scala.concurrent.duration._
import scala.io.StdIn

object AkkaHttpServer extends App {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val timeout = Timeout(5 seconds)

    //Denis: location/{dyn_id}/(update|delete|create)/status/{v1}
    //Chong: location/{dyn_id}/status/{v1}/....

    val actorRef = system.actorOf(Props[SimpleActor], "simpleActor")
    val route =
        path("foo") {
            get {
                actorRef ! "MessageFromServer"
                complete("Hello")
            }
        } ~ path("bar") {
            get {
                complete("Bar")
            }
            delete {
                complete("Deleted Bar")
            }
        } ~ path("location" / IntNumber / "status" / IntNumber / """rich(\w+)""".r) { (id, v, rp) =>
             get {
                 complete(s"I receive an url for id: $id, and version: $v, with path of $rp")
             }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
}
