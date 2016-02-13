package com.betgenius

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.betgenius.EchoActor.EchoMessage
import com.betgenius.model.{Market, SportingFixture}
import com.betgenius.repository.EntityManager.Persist
import org.joda.time.DateTime

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask
import scala.concurrent.duration._
import scala.xml.NodeSeq

/**
  * Created by douglas on 06/02/16.
  */
trait BetGenius extends ScalaXmlSupport {

  implicit val fixtureUnmarshaller:Unmarshaller[HttpEntity,SportingFixture]

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
         entity(as[SportingFixture]) { fixture =>
             complete {
               (entityActor ? Persist(fixture)).mapTo[String]
             }
         }
      }
    }

}
