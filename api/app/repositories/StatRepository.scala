package repositories

import java.sql.Timestamp

import org.joda.time.DateTime
import tools.Models._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Postrgre repository, it saves/updates data into postgresql DB
 */
object StatRepository extends PostgresDBComponent with StateTable {
  this: PostgresDBComponent =>

  import driver.api._


  /**
   *
   */
  private def insertOrUpdateAll(stat: Seq[Stats]): Future[Seq[Int]] =
    db.run(DBIO.sequence(stat.map { s =>
      statsQuery += s
    }).transactionally)


  def saveOrUpdate(states: scala.collection.mutable.HashMap[CustomerContent, HttpRequest])(implicit executionContext: ExecutionContext): Future[Seq[Int]] = {

    val result = states.map {
      case (CustomerContent(customer, content), HttpRequest(token, _, _, _, p2p, cdn, _)) =>
        val sessions = token.split(",").length //number of sessions
        val dateTime = new Timestamp(DateTime.now.getMillis)
        Stats(
          customer, content, dateTime, p2p, cdn, sessions
        )
    }.toList
    insertOrUpdateAll(result)
  }

}
