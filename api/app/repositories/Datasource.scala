package repositories

import java.sql.Timestamp

import org.joda.time.DateTime
import play.api.Logger
import tools.Models._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

/**
 * Postrgre repository, it saves/updates data into postgresql DB
 */
object Datasource extends PostgresDBComponent with StateTable {
  this: PostgresDBComponent =>
  import driver.api._

  //a global HashMap to save Data. (Memory Cache)
  var states = scala.collection.mutable.HashMap[CustomerContent, HttpRequest]() //can be volatile, or a concurrentMap

  def saveOrUpdate(states: scala.collection.mutable.Map[CustomerContent, HttpRequest]) = {

    states.map {
      case (CustomerContent(customer, content), HttpRequest(token, _, _, _, p2p, cdn, _)) =>
        val sessions = token.split(",").length //number of sessions
        val dateTime = new Timestamp(DateTime.now.getMillis)

        insertOrUpdate(Stats(
          customer, content, dateTime, p2p, cdn, sessions
        )).recover {
          case e => Logger.error(s"cannot save row $customer, $content, because of $e")
            Future.successful(0)
        }
    }
  }


  private def insertOrUpdate(stat: Stats): Future[Int] =
    db.run(statsQuery.insertOrUpdate(stat))

}
