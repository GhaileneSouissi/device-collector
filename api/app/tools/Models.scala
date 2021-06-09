package tools

import java.sql.Timestamp

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

}


