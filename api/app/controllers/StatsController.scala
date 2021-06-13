package controllers

import actors.Implicits
import akka.actor.ActorSystem
import akka.stream.Materializer
import io.swagger.annotations._
import javax.inject._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, Request, _}
import tools.Models
import tools.Models._
import tools.SerializationImplicits._

import scala.concurrent.{ExecutionContext, Future}

@Api(value = "/api/v1/stats",
  tags = Array("device collector"),
  produces = "application/json")
@Singleton
class StatsController @Inject()(components: ControllerComponents)
                               (implicit ec: ExecutionContext, materializer: Materializer, system: ActorSystem)
  extends AbstractController(components) with Implicits {

  @ApiOperation(value = "save request in a memory cache, aggregate if needed", httpMethod = "POST", response = classOf[String])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", required = true, dataType = "object", paramType = "body")
  ))
  @ApiResponses(Array(new ApiResponse(code = 200, message = "saved correctly !"),
    new ApiResponse(code = 500, message = "undefined error")))
  def saveStats: Action[JsValue] =
    Action.async[JsValue](parse.json[JsValue]) {
      implicit request: Request[JsValue] =>
        import akka.actor.typed.scaladsl.AskPattern._
        request.body.validate[HttpRequest] match {
          case JsSuccess(httpRequest, _) =>
            val result: Future[CachedHttpRequest] = actorRef.ask(ref => Models.CacheOrUpdate(httpRequest, ref))
            result.map(res => Ok(Json.toJson(res.devices)))

          case error: JsError => Future.successful(BadRequest(Json.toJson(error)))
        }
    }
}