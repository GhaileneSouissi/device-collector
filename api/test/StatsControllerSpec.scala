import org.scalatest.{BeforeAndAfter, GivenWhenThen}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, route, status, _}
import play.api.{Application, Configuration}
import play.test.WithApplication

class StatsControllerSpec extends PlaySpec
  with GivenWhenThen
  with BeforeAndAfter
  with GuiceOneAppPerSuite {

  val application: Application = GuiceApplicationBuilder().build()
  val configuration: Configuration = application.configuration
  val endpoint = "/api/v1/stats"


  "stats" should {
    "return error" when {
      "it is called without Json " in new WithApplication {
        val fakeRequest = FakeRequest(POST, endpoint).withJsonBody(JsNull)
        val maybeResponse = route(application, fakeRequest)

        assume(maybeResponse.isDefined)
        Then("It is a 400 status code")

        val result = maybeResponse.get
        status(result) mustBe BAD_REQUEST

      }
    }
  }

  "stats" should {
    "return error" when {
      "it is called with an empty Json " in new WithApplication {
        val fakeRequest = FakeRequest(POST, endpoint).withJsonBody(JsArray())
        val maybeResponse = route(application, fakeRequest)

        assume(maybeResponse.isDefined)
        Then("It is a 400 status code")

        val result = maybeResponse.get
        status(result) mustBe BAD_REQUEST

      }
    }
  }


  "stats" should {
    "return error" when {
      "it is called with json" in new WithApplication {
        Given("json with missing mandatory fields")


        val validRequestJson = JsObject(Seq(
          "customer" -> JsString("Chaine1"),
          "content" -> JsString("Prog1")
        ))

        val fakeRequest = FakeRequest(POST, endpoint).withJsonBody(validRequestJson)
        val maybeResponse = route(application, fakeRequest)

        assume(maybeResponse.isDefined)
        Then("It is a 400 status code")

        val result = maybeResponse.get
        status(result) mustBe BAD_REQUEST

      }
    }
  }


  "stats" should {
    "return Ok" when {
      "it is called with a valid Json with optional fields " in new WithApplication {
        Given("json without missing mandatory fields")
        val validRequestJson = JsObject(Seq(
          "token" -> JsString("c98arf53-ae39-4c9d-af44-c6957ee2f748"),
          "customer" -> JsString("Chaine1"),
          "content" -> JsString("Prog1"),
          "timespan" -> JsNumber(30000),
          "p2p" -> JsNumber(21123),
          "cdn" -> JsNumber(560065),
          "sessionDuration" -> JsNumber(120000)
        ))


        val fakeRequest = FakeRequest(POST, endpoint).withJsonBody(validRequestJson)
        val maybeResponse = route(application, fakeRequest)

        assume(maybeResponse.isDefined)
        Then("It is a 200 status code")

        val result = maybeResponse.get
        status(result) mustBe OK

      }
    }
  }


}
