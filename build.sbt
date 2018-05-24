

name := "akka-training"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.11"

val akkaHttpVersion = "10.1.10"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.1.0",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0",
    "com.typesafe.akka" %% "akka-http-xml" % "10.1.0",
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "org.slf4j" % "slf4j-simple" % "1.7.25",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
