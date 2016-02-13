name := "recs-api3"

version := "1.0"

scalaVersion := "2.11.7"

lazy val akkaVersion = "2.4.2-RC2"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaVersion,
//  "io.spray" %% "spray-can" % "1.3.3",
//  "io.spray" %% "spray-routing" % "1.3.3",
//  "io.spray" %% "spray-caching" % "1.3.3",
//  "io.spray" %% "spray-json" % "1.3.2",
//  "io.spray" %% "spray-client" % "1.3.3",
//  "io.spray" %% "spray-testkit" % "1.3.3",
  "joda-time" % "joda-time" % "2.8.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.joda" % "joda-convert" % "1.7"
)


mainClass in assembly := Some("com.betgenius.Application")

//Revolver.settings
//
//mainClass in Revolver.reStart := Some("com.betgenius.Application")
    
