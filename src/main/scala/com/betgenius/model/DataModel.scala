package com.betgenius.model

import akka.http.javadsl.server.Unmarshaller

import scala.xml.NodeSeq

case class PersistenceResult(status:String)

object SportingFixture {
      def fromXml(node:NodeSeq) = {
           val id = (node \ "Id").text
           val name = (node \ "Name").text
           SportsFixture(id,name,None)
      }
}




