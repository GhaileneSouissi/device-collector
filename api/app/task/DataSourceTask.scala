package task

import actors.Implicits
import akka.actor.typed.scaladsl.AskPattern._
import javax.inject.Inject
import tools.Models

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
 * a scheduled task to save data into DB and clear memory cache
 **/
class DataSourceTask @Inject()(implicit executionContext: ExecutionContext) extends Implicits {

  system.scheduler.scheduleAtFixedRate(1.minutes, 1.minutes) { () =>
    val result: Future[Models.StoreResponse] = actorRef.ask(ref => Models.StoreAndClear(ref))
  }
}
