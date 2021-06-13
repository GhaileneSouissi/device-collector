package filters

import akka.stream.Materializer
import javax.inject.Inject
import play.api.{Logger, Logging}
import play.api.mvc.{Filter, RequestHeader, Result}
import play.api.routing.{HandlerDef, Router}

import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {
  // As a share variable, atomicity should be good but expensive for this case.
  private /* @volatile */ var countLog = 0

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      if (requestHeader.attrs.contains(Router.Attrs.HandlerDef)) {
        val handlerDef: HandlerDef = requestHeader.attrs(Router.Attrs.HandlerDef)
        val action = handlerDef.controller + "." + handlerDef.method
        // Limit duration time (500ms) by request or limit value to log every 100 times.
        if (requestTime >= 500L || countLog % 100 == 0)
          logger.info(s"$action took ${requestTime}ms and returned ${result.header.status}")
      }
      countLog += 1 // This is for log only.
      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
