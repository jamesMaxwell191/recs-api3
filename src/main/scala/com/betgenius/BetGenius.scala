package com.betgenius

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.pattern.ask
import akka.util.Timeout
import com.betgenius.EchoActor.EchoMessage
import com.betgenius.model.SportsFixture
import com.betgenius.repository.EntityManager.Persist

import scala.concurrent.duration._

/**
  * Created by douglas on 06/02/16.
  */
trait BetGenius extends ScalaXmlSupport {

  implicit val fixtureUnmarshaller:Unmarshaller[HttpEntity,SportsFixture]

  val echoActor:ActorRef

  val entityActor : ActorRef

  implicit val theTimeout = Timeout(15 seconds)


  val fixtureDataRoute = pathPrefix("betgenius") {
    get {
      path("tony") {
        complete {
          "hello world"
        }
      } ~
        path("ping") {
          complete {
            "pong"
          }
        } ~
        path("crash") {
          complete {
            "crash"
          }
        }
    }
  } ~
    pathSingleSlash {
      get {
        complete {
          (echoActor ? EchoMessage("joe")).mapTo[String]
        }
      } ~
      post {
         entity(as[SportsFixture]) { fixture =>
             complete {
               (entityActor ? Persist(fixture)).mapTo[String]
             }
         }
      }
    }

}
