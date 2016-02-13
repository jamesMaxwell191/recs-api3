package com.betgenius.repository

import akka.actor.{ActorLogging, Props, Actor}
import com.betgenius.model.SportingFixture
import com.betgenius.repository.EntityManager.Persist

/**
  * Created by douglas on 06/02/16.
  */
class EntityManager extends Actor with ActorLogging{

     override def receive = {
       case Persist(fixture) => sender ! "Ack entity persisted"

       case s:String => println(s"got $s in the EntityManager")
     }

}

object EntityManager {

    def props = Props[EntityManager]

    case class Persist(fixture:SportingFixture)
}
