package com.betgenius

import akka.actor.{ActorLogging, Props, Actor}
import akka.cluster.client.{ClusterClientSettings, ClusterClient}
import com.betgenius.EchoActor.EchoMessage
import com.betgenius.model.{SportsFixture, PersistenceResult}
import akka.pattern.ask

/**
  * Created by douglas on 06/02/16.
  */
class EchoActor extends Actor with ActorLogging{

  val client = context.actorOf(ClusterClient.props(ClusterClientSettings(context.system)), "clusterClient")

     override def receive = {
       case EchoMessage(text) => println("in the echo actor , updating the cluster")


       case SportsFixture(_,_,_) => println("received a sports fixture")
         client.(ClusterClient.Send("/user/sportingFixtureService", "3:Rangers vs Celtic", localAffinity = true), self)

       case s:String => log.info("got a response from the cluster")
     }

}

object EchoActor {
     def props = Props[EchoActor]

     case class EchoMessage(text:String)
}
