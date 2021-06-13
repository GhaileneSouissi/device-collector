package actors

import akka.actor.ActorSystem
import akka.actor.typed.Scheduler
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Implicits parameters related to akka Actor System
 */
trait Implicits {

  import akka.actor.typed.scaladsl.adapter._

  implicit val system: ActorSystem = ActorSystem("actorSystem")

  implicit val timeout: Timeout = 5.seconds
  implicit val scheduler: Scheduler = system.toTyped.scheduler
  implicit val dispatcher: ExecutionContext = system.dispatcher

  val actorRef = system.spawn(CacheActor.cacheOrStore().behavior, "CacheActor")

}

