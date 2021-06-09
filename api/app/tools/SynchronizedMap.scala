package tools

import tools.Models.{CustomerContent, HttpRequest}

import scala.collection.mutable

/**
 * A thread-safe HashMap
 */
object SynchronizedMap {

  implicit class SynchronizedMap(map: mutable.HashMap[CustomerContent, HttpRequest]) {
    def addSynchronized(element: HttpRequest) = {
      synchronized {
        map += CustomerContent(element.customer, element.content) -> element
      }
    }

    def getSynchronized(key: CustomerContent) =
      synchronized {
        map.get(key)
      }

    def clearSynchronized()= {
      map.clear()
    }


  }

}
