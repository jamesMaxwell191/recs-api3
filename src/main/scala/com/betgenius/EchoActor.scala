package com.betgenius

import akka.actor.{ActorLogging, Props, Actor}
import akka.cluster.client.{ClusterClientSettings, ClusterClient}
import akka.util.Timeout
import com.betgenius.EchoActor.EchoMessage
import com.betgenius.model.{SportsFixture, PersistenceResult}
import akka.pattern.{ask, pipe}
import scala.concurrent.duration._

/**
  * Created by douglas on 06/02/16.
  */
class EchoActor extends Actor with ActorLogging{

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(10 seconds)

  val client = context.actorOf(ClusterClient.props(ClusterClientSettings(context.system)), "clusterClient")

     override def receive = {
       case EchoMessage(text) => println("in the echo actor , updating the cluster")


       case SportsFixture(_,_,_) => println("received a sports fixture")
         val result = (client ?  ClusterClient.Send("/user/sportingFixtureService", "3:Rangers vs Celtic", localAffinity = true)).mapTo[String]
         result.map(PersistenceResult(_)) pipeTo sender
     }

}

object EchoActor {
     def props = Props[EchoActor]

     case class EchoMessage(text:String)
}
