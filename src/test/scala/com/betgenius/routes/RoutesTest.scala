package com.betgenius.routes

import akka.http.scaladsl.model.{StatusCodes, StatusCode}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.betgenius.{BetGenius, Application}
import org.scalatest.{Matchers, MustMatchers, WordSpec}

/**
  * Created by douglas on 05/02/16.
  */
class RoutesTest extends WordSpec with Matchers with ScalatestRouteTest with BetGenius {

    "the main route" should {
         "respond with hello world" in {
              Get("/betgenius/tony") ~> fixtureDataRoute ~> check {
                responseAs[String] shouldEqual "hello world"
              }
         }
      "respond with pong" in {
        Get("/betgenius/ping") ~> fixtureDataRoute ~> check {
          responseAs[String] shouldEqual "pong"
        }
      }
      "respond with ok status code" in {
        Get("/betgenius/crash") ~> fixtureDataRoute ~> check {
          status === StatusCodes.Success
        }
      }
      "respond with application root" in {
        Get("/") ~> fixtureDataRoute ~> check {
          responseAs[String] shouldEqual "application root"
        }
      }
    }

}
