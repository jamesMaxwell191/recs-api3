package com.betgenius.model

import akka.http.javadsl.server.Unmarshaller

import scala.xml.NodeSeq

/**
  * Created by douglas on 06/02/16.
  */
case class SportingFixture(id:String, name:String,status:String ,sport:Sport,competition: Competition) {

}

object SportingFixture {
      def fromXml(node:NodeSeq) = {
           val id = (node \ "Id").text
           val name = (node \ "Name").text
           val status = (node \ "Status").text
           val sport = Sport.fromXml(node \ "Sport")
           val competition = Competition.fromXml(node \ "Competition")
           SportingFixture(id,name,status,sport,competition)
      }
}


case class Sport(id:String, name:String)

object Sport {
     def fromXml(node:NodeSeq) = {
       val id = (node \ "Id").text
       val name = (node \ "Name").text
       Sport(id, name)
     }
}

case class Competition(id:String,name:String)

object Competition {
    def fromXml(node:NodeSeq) = {
      val id = (node \ "Id").text
      val name = (node \ "Name").text
      Competition(id, name)
    }
}

case class Market(name:String)

case class PersistenceResult(state:String)

