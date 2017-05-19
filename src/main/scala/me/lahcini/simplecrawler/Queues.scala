import java.util.Calendar

import scala.collection.mutable.ListBuffer
import scala.util.Random

trait TQueue {
  val queue:ListBuffer[TCanonicalUrl] = new ListBuffer()
  def isEmpty = queue.isEmpty
  def get: TCanonicalUrl
  // add new URLs to the end of the Queue
  def enqueue(url : String) = {
    queue += CanonicalURL(url)
  }
}

trait TRandomSelectionQueue extends TQueue {
  override def get = {
    // select a random index between 0 and len
    val r = new Random(Calendar.getInstance().getTimeInMillis)
    val i = r.nextInt(queue.size)
    queue.remove(i) // get the i-th element
  }
}

class RandomSelectionQueue extends TQueue with TRandomSelectionQueue