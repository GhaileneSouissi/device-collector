package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import io.swagger.annotations._
import javax.inject._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, Request, _}
import repositories.Datasource
import tools.Models._
import tools.SerializationImplicits._
import tools.SynchronizedMap._

import scala.concurrent.{ExecutionContext, Future}

@Api(value = "/api/v1/stats",
  tags = Array("device collector"),
  produces = "application/json")
@Singleton
class StatsController @Inject()(components: ControllerComponents)
                               (implicit ec: ExecutionContext, system: ActorSystem, materializer: Materializer)
  extends AbstractController(components) {

  @ApiOperation(value = "save request in a memory cache, aggregate if needed", httpMethod = "POST", response = classOf[String])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", required = true, dataType = "object", paramType = "body")
  ))
  @ApiResponses(Array(new ApiResponse(code = 200, message = "saved correctly !"),
    new ApiResponse(code = 500, message = "undefined error")))
  def saveStats: Action[JsValue] =
    Action.async[JsValue](parse.json[JsValue]) {
      implicit request: Request[JsValue] =>

        request.body.validate[HttpRequest] match {
          case JsSuccess(httpRequest, _) =>
            val res = Datasource.states.getSynchronized(CustomerContent(httpRequest.customer, httpRequest.content)) match {
              case Some(request) =>
                val updatedRequest = HttpRequest(
                  token = aggregate(request.token, httpRequest.token),
                  customer = httpRequest.customer,
                  content = httpRequest.content,
                  timespan = request.timespan + httpRequest.timespan,
                  cdn = request.cdn + httpRequest.cdn,
                  p2p = request.p2p + httpRequest.p2p,
                  sessionDuration = request.sessionDuration + httpRequest.sessionDuration
                )
                Datasource.states addSynchronized updatedRequest
              case _ => Datasource.states addSynchronized httpRequest
            }
            Future.successful(Ok(Json.toJson(res)))
          case error: JsError => Future.successful(BadRequest(Json.toJson(error)))
        }
    }

  /**
   * count sessions number per customer/content
   */
  private def aggregate(attribue: String, newEelem: String): String = {
    if (!attribue.split(",").toSet.contains(newEelem)) attribue.concat(s",${newEelem}")
    else attribue
  }

}