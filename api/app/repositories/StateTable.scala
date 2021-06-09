package repositories

import java.sql.Timestamp

import tools.Models._

/**
 * Stats Table
 */
trait StateTable extends PostgresDBComponent {
  this: PostgresDBComponent =>

  import driver.api._

  class StatsTable(tag: Tag) extends Table[Stats](tag, "stats") {

    def customer = column[String]("customer", O.PrimaryKey)

    def content = column[String]("content", O.PrimaryKey)

    def datetime = column[Timestamp]("datetime")

    def cdn = column[Int]("cdn")

    def p2p = column[Int]("p2p")

    def sessions = column[Int]("sessions")


    def * = (
      customer,
      content,
      datetime,
      cdn,
      p2p,
      sessions,
      ).mapTo[Stats]
  }

  val statsQuery = TableQuery[StatsTable]


}
