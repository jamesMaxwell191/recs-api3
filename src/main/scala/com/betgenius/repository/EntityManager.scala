package com.betgenius.repository

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import akka.util.Timeout
import com.betgenius.model.{PersistenceResult, SportsFixture}
import com.betgenius.repository.EntityManager.Persist
import scala.concurrent.duration._
import akka.pattern.{ask,pipe}

/**
  * Created by douglas on 06/02/16.
  */
class EntityManager extends Actor with ActorLogging{

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(10 seconds)

  val client = context.actorOf(ClusterClient.props(ClusterClientSettings(context.system)), "clusterClient")

  override def receive = {

    case Persist(fixture) => println("received a sports fixture")
      val result = (client ?  ClusterClient.Send("/user/sportingFixtureService", fixture , localAffinity = true)).mapTo[String]
      result.map(PersistenceResult(_)) pipeTo sender
  }

}

object EntityManager {

    def props = Props[EntityManager]

    case class Persist(fixture:SportsFixture)
}
