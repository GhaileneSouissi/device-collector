package tools

import play.api.libs.json.{JsArray, JsError, JsObject, JsPath, JsResult, JsString, JsSuccess, JsValue, KeyPathNode, Reads, Writes}
import tools.Models._

/**
 * Http Response/Request Serialisation
 */
object SerializationImplicits {
  implicit val HttpRequestReads: Reads[HttpRequest] = new Reads[HttpRequest] {
    override def reads(json: JsValue): JsResult[HttpRequest] = {

      val KnownFields = Set("token", "customer", "content", "p2p", "cdn", "timespan","sessionDuration")

      json match {
        case obj: JsObject if obj.fields.nonEmpty =>
          val maybeMissingAttribute = KnownFields diff obj.keys
          val maybeUnknownAttribute = obj.keys diff KnownFields
          (maybeMissingAttribute.size, maybeUnknownAttribute.size) match {
            case (0, 0) => JsSuccess(HttpRequest(
              token = (json \ "token").as[String],
              customer = (json \ "customer").as[String],
              content = (json \ "content").as[String],
              timespan = (json \ "timespan").as[Int],
              p2p = (json \ "p2p").as[Int],
              cdn = (json \ "cdn").as[Int],
              sessionDuration = (json \ "sessionDuration").as[Int]
            ))
            case (0, _) =>
              error(s"Unknown attributes [${
                maybeUnknownAttribute.mkString(",")
              }]")
            case (_, 0) =>
              error(s"Missing attributes [${
                maybeMissingAttribute.mkString(",")
              }]")
            case _ => error("Undefined Error")

          }
        case _ =>
          error("Empty Json File")
      }
    }
  }

  implicit val HttpRequestWrites: Writes[HttpRequest] = new Writes[HttpRequest] {
    override def writes(httpRequest: HttpRequest): JsValue  = {
      val result: Seq[(String, play.api.libs.json.JsString)] = Seq(
        "sessions" -> JsString(httpRequest.token.split(",").length.toString),
        "p2p" -> JsString(httpRequest.p2p.toString),
        "cdn" -> JsString(httpRequest.cdn.toString),
        "sessionDuration" -> JsString(httpRequest.sessionDuration.toString),
        "timespan" -> JsString(httpRequest.timespan.toString)
      )
      JsObject(result)
    }
  }

  implicit val advancedSearchQueryWrites1: Writes[CustomerContent] = new Writes[CustomerContent] {
    override def writes(httpRequest: CustomerContent): JsValue  = {
      val result: Seq[(String, play.api.libs.json.JsString)] = Seq(
        "customerContent" -> JsString(s"${httpRequest.customer},${httpRequest.content}")
      )
      JsObject(result)
    }
  }

  private def error(msg: String): JsError = {
    JsError(
      error = msg
    )
  }

  implicit val jsErrorWrites: Writes[JsError] = new Writes[JsError] {
    override def writes(jsError: JsError): JsValue = {
      JsArray(jsError.errors.map(err =>
        JsObject(Seq(
          "field" -> JsString(err._1.toString),
          "message" -> JsString(err._2 match {
            case Nil => "Unknow Error"
            case _ => err._2.headOption.get.message
          })))))
    }
  }
}