package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import repositories.StatRepository
import tools.Models._

import scala.concurrent.ExecutionContext

/**
 * Cache actor, it is responsible for receiving messages to update memory cache and save data to DB
 */
object CacheActor {

  private var states: scala.collection.mutable.HashMap[CustomerContent, HttpRequest] = scala.collection.mutable.HashMap.empty

  /**
   * actor behavior:
   * 1- CacheOrUpdate: receive message to add or update cache
   * 2- StoreAndClear: receive message to save cache to DB
   */
  def cacheOrStore()(implicit executionContext: ExecutionContext): Behavior[CacheOps] =
    Behaviors.receive { (_, message) =>
      message match {
        case getCatch @ CacheOrUpdate(httpRequest, _) =>
          val res = states.get(CustomerContent(httpRequest.customer, httpRequest.content)) match {
            case Some(request) =>
              val updatedRequest = aggregateRequest(httpRequest, request)
              states += (CustomerContent(request.customer, request.content) -> updatedRequest)
            case _ => states += (CustomerContent(httpRequest.customer, httpRequest.content) -> httpRequest)
          }
          getCatch.replyTo ! CachedHttpRequest(res)
          Behaviors.same

        case store @ StoreAndClear(_) =>
          for {
            _ <- StatRepository.saveOrUpdate(states) //one saved in DB, clear memory cache
            b = states.clear()
          } yield b
          store.replyTo ! StoreResponse("Done")
          Behaviors.same
      }
    }


  private def aggregate(attribue: String, newElem: String): String = {
    if (!attribue.split(",").toSet.contains(newElem)) attribue.concat(s",$newElem")
    else attribue
  }

  private def aggregateRequest(httpRequest: HttpRequest, request: HttpRequest) =
    HttpRequest(
      token = aggregate(request.token, httpRequest.token),
      customer = httpRequest.customer,
      content = httpRequest.content,
      timespan = request.timespan + httpRequest.timespan,
      cdn = request.cdn + httpRequest.cdn,
      p2p = request.p2p + httpRequest.p2p,
      sessionDuration = request.sessionDuration + httpRequest.sessionDuration
    )


}
