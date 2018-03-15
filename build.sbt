

name := "akka-training"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.11"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "org.slf4j" % "slf4j-simple" % "1.7.25",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
