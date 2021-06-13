package tools

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object AppSettings {
  val configuration = ConfigFactory.load();

  val SchedularInterval = configuration.getDuration("common.schedular.interval", MINUTES).minutes

}
