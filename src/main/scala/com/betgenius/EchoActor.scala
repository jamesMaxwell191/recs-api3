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

  implicit val timeout = Timeout(10 seconds)

     override def receive = {
       case EchoMessage(text) => log.info(s"echoing $text")

       case s:String => log.info(s"received message $s")

     }

}

object EchoActor {
     def props = Props[EchoActor]

     case class EchoMessage(text:String)
}
