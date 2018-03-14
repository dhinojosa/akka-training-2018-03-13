package com.sbux.futures

import java.time.{LocalDate, LocalDateTime}
import java.util.concurrent.{Executors, TimeUnit}

import akka.actor.ActorSystem
import org.scalatest.{FunSuite, Matchers}

import scala.collection.{GenTraversableOnce, immutable}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

class FuturesSpec extends FunSuite with Matchers {
   test("A future in scala") {
       //implicits to bring a thread pool
       import scala.concurrent.ExecutionContext.Implicits.global
       val f = Future {
         println("Line 11: " + Thread.currentThread().getName)
         Thread.sleep(4000)
         40 + 100
       }

       f.foreach(r => println(r))

       println("Line 17: " + Thread.currentThread().getName)

       Await.ready(f, Duration.apply(10, TimeUnit.SECONDS))
   }

  test("Creating a thread pool from Java Executor") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext = ExecutionContext.fromExecutor(executorService)

    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    future.foreach(println)

    Await.ready(future, Duration.apply(10, TimeUnit.SECONDS))

    executorService.shutdown()
  }

  test("Creating a thread pool from ActorSystem and its dispatcher") {
    val actorSystem = ActorSystem("FooSystem")
    import actorSystem.dispatcher

    val future = Future {
      Thread.sleep(1000)
      10 + 40
    }

    future.map(x => x + 10).onComplete {
      case Success(i) => println("OK!" + i)
      case Failure(throwable) => println("Oh no" + throwable.getMessage)
    }


    Await.ready(future, Duration.apply(10, TimeUnit.SECONDS))
  }

  test("A Promise") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext = ExecutionContext.fromExecutor(executorService)

    val promise:Promise[Int] = Promise.apply()

    //This is what I will hand out to whoever
    val future = promise.future
    future.onComplete {
      case Success(i) => println("OK!" + i)
      case Failure(throwable) => println("Oh no" + throwable.getMessage)
    }

    Thread.sleep(4000)
    promise.success(42)

    Await.ready(future, Duration.apply(10, TimeUnit.SECONDS))
  }

  test("Monadic Properties") {
      val m:Option[Int] = Some(12)
      val n:Option[Int] = Some(20)

      val o: Option[Int] = m.map(x => x + 10)
      println(o)

      val p: Option[Int] = m.flatMap(x => Some(x + 40))

      val q  = List(1,2,3)
      val r  = q.flatMap(x => List(-x, x, x + 1))
       println(r)
  }

  test("Test Future Monad") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext = ExecutionContext.fromExecutor(executorService)

    val f: Future[Int] = Future{10 + 40}

    //val result1: Future[Int] = f.flatMap(x => Future{x + 100})

    val result2: Future[Int] = for (x <- f) yield x + 100
    result2.foreach(println)
  }

  test("Test Future Monad Combinations") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext = ExecutionContext.fromExecutor(executorService)

    val f1:Future[Int] = Future{
      Thread.sleep(4000)
      10 + 40
    }

    val f2:Future[Int] = Future {
      Thread.sleep(2500)
      150
    }

    //flatMap or forComprehension
    val f3:Future[Int] = f1.flatMap(x => f2.map(y => x + y))
    val f4:Future[Int] = for (x <- f1; y <- f2) yield x + y

    f3.onComplete {
      case Success(i) => println(i) //200
      case Failure(t) => t.printStackTrace()
    }

    Await.ready(f3, Duration.apply(10, TimeUnit.SECONDS))
  }

  test("Future of Lists of stuff") {
    val executorService = Executors.newFixedThreadPool(5)
    implicit val executionContext = ExecutionContext.fromExecutor(executorService)

    val futures: Seq[Future[LocalDateTime]] = List.fill(10) {
      Future {
        LocalDateTime.now()
      }
    }

    Thread.sleep(10000)
    val eventualDates: Future[Seq[LocalDateTime]] = Future.sequence(futures)
    val eventualInt = eventualDates.map(dates => dates.map(d => d.getSecond).sum)
    eventualInt.foreach(println)
    Await.ready(eventualInt, Duration(10, TimeUnit.SECONDS))
  }

  test("Proper parse") {
    def parseNumber(s:String):Try[Int] = Try{Integer.parseInt(s)}
    //val triedInt = parseNumber("40").map(x => x + 30)
    val triedInt = for (i <- parseNumber("40")) yield i + 30
    println(triedInt.getOrElse(-1))
  }
}
