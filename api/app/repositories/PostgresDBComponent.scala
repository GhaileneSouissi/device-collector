package repositories

import slick.jdbc.PostgresProfile

trait PostgresDBComponent {

  val driver = PostgresProfile
  val db = PostgresDBComponent.connectionPool //connection pool

}

/**
 * Data base configuration
 */
object PostgresDBComponent {

  import slick.jdbc.PostgresProfile.api._
  val connectionPool = Database.forConfig("common.postgres")
}
