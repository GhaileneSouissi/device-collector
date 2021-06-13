package tools

import java.sql.Timestamp

import akka.actor.typed.ActorRef

object Models {

  case class HttpRequest(token: String,
                         customer: String,
                         content: String,
                         timespan: Int,
                         p2p: Int,
                         cdn: Int,
                         sessionDuration: Int)

  case class Stats(customer: String,
                   content: String,
                   datetime: Timestamp,
                   p2p: Int,
                   cdn: Int,
                   sessions: Int)

  case class CustomerContent(customer: String, content: String)

  sealed trait CacheOps

  final case class CacheOrUpdate(httpRequest: HttpRequest, replyTo: ActorRef[CachedHttpRequest]) extends CacheOps
  final case class StoreAndClear(replyTo: ActorRef[StoreResponse]) extends CacheOps


  final case class CachedHttpRequest(devices: scala.collection.mutable.HashMap[CustomerContent, HttpRequest])
  final case class StoreResponse(message: String)


}


