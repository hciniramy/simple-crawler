import java.io.{File, FileWriter}

import scala.collection.mutable

trait TScheduler {
  def next: TCanonicalUrl
  def schedule(url: TCanonicalUrl)
  def isEmpty: Boolean
}

trait TSchedulerMonitoring  {
  val dir = "logs/scheduler"
  def log(u:TCanonicalUrl): Unit = {
    val filename = dir
    val writer = new FileWriter(new File(filename), true) ;
    writer.write("#ID="+ HashMD5.md5Hash(u.canonize)+";" + u.canonize +"\n")
    writer.close
  }
}

class SimpleScheduler extends TScheduler with TSchedulerMonitoring{
  val s = new mutable.Queue[TCanonicalUrl]()
  override def next: TCanonicalUrl = {
    val u = s.dequeue()
    log(u)
    u
  }
  override def schedule(url: TCanonicalUrl): Unit = s.enqueue(url)
  override def isEmpty = s.isEmpty
}