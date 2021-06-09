package task

import java.time.LocalDateTime

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.Logger
import repositories.Datasource
import tools.AppSettings
import tools.SynchronizedMap._

import scala.concurrent.ExecutionContext

/**
 * a scheduled task to save data into DB and clear memory cache
 *
 * @param actorSystem
 * @param executionContext
 */
class DataSourceTask @Inject()(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {

  actorSystem.scheduler.schedule(initialDelay = AppSettings.SchedularInterval, interval = AppSettings.SchedularInterval) {
    for {
      _ <- Datasource.saveOrUpdate(Datasource.states)
      b = Datasource.states.clearSynchronized()
    } yield b

    Logger.info(s"saving files, and clearing cache... ${LocalDateTime.now()}")
  }
}
