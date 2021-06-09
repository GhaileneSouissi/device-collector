package controllers

import io.swagger.annotations.{ApiResponse, ApiResponses}
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class TechnicalController @Inject()(components: ControllerComponents)
                                   (implicit ec: ExecutionContext)
  extends AbstractController(components) {

  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Application is healthy"),
    new ApiResponse(code = 400, message = "Bad request"),
    new ApiResponse(code = 500, message = "Internal Error")
  ))
  def getHealth = Action {
    Ok("Application is healthy")
  }
}