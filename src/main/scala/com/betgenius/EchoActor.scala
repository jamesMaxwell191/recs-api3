package com.betgenius

import akka.actor.{Props, Actor}
import com.betgenius.EchoActor.EchoMessage
import com.betgenius.model.PersistenceResult
import org.joda.time.DateTime

/**
  * Created by douglas on 06/02/16.
  */
class EchoActor extends Actor{

     override def receive = {
       case EchoMessage(text) => println("sleeping in the echo actor " + DateTime.now); Thread.sleep(10000); sender ! s"hello there $text"

       case _ => println("echoing a persistence result")
                           sender ! PersistenceResult("Joe")
     }

}

object EchoActor {
     def props = Props[EchoActor]

     case class EchoMessage(text:String)
}
