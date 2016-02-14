package com.betgenius

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, FlowShape, Fusing}
import akka.util.Timeout
import com.betgenius.model.{PersistenceResult, SportingFixture, SportsFixture}
import com.betgenius.repository.EntityManager
import com.betgenius.repository.EntityManager.Persist

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * This spray applicaton exposes a rest web service which provides
  * program recommendations for a sky subscriber
  * The results returned by the service are configurable in application.conf as is
  * the service itself and the remote rest application providing the actual recommendations
  *
  * The results returned by the service are cached. The way caching is currently done
  * means that if a request is made and the remote server is down the error result will
  * go in the cache.
  * If the remote server comes back up it will not be accessible for an user in the cache
  * until the cached values expires.
  */
object Application extends ScalaXmlSupport {


  def main(args:Array[String]): Unit ={
    implicit val actorSystem = ActorSystem("recs")

    implicit val materializer = ActorMaterializer()

    implicit val timeout = Timeout(60 seconds)

    implicit val fixtureUnmarshaller = defaultNodeSeqUnmarshaller.map(SportingFixture.fromXml(_))


    lazy val echoActor = actorSystem.actorOf(RoundRobinPool(5).props(EchoActor.props), "echoRouter")

    lazy val persister = actorSystem.actorOf(RoundRobinPool(5).props(EntityManager.props), "persistenceRouter")


    val broadcastPersistenceResult =
      Flow.fromGraph(GraphDSL.create() { implicit b =>
        import GraphDSL.Implicits._
        val broadcast = b.add(Broadcast[PersistenceResult](2))
        broadcast.out(0).map(convertPersistentEntity(_)) ~> Sink.actorRef(echoActor,"gtpRouter")
        FlowShape(broadcast.in, broadcast.out(1))
      })


    val flow = Flow[HttpRequest].mapAsync(4){
      case HttpRequest(HttpMethods.POST,Uri.Path("/"),headers,body,protocol) =>  fixtureUnmarshaller(body)
    }.mapAsync(2){
      case fix @ SportsFixture(_,_,_) => (persister ? Persist(fix)).mapTo[PersistenceResult]
    }.via(broadcastPersistenceResult).map(f => HttpResponse(200, entity="successfully persisted the fixture"))

    val flow2 = Fusing.aggressive(flow)

    val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
      Http().bind(interface = "localhost", port = 9095)

    val bindingFuture: Future[Http.ServerBinding] =
      serverSource.to(Sink.foreach { connection => // foreach materializes the source
        println("Accepted new connection from " + connection.remoteAddress)
        connection.handleWith(flow)
      }).run()

    bindingFuture onFailure {
      case e:Exception => println("failed to bind " + e)
        actorSystem.terminate()
    }
  }

  def convertPersistentEntity(result:PersistenceResult) ={
        println("converted the persistence result")
        "well done"
  }

}
