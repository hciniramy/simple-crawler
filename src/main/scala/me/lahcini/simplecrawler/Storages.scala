import java.io.{File, PrintWriter}

trait TStorage {
  def store(url: TCanonicalUrl, d:String)
}

class ConsoleStorage extends TStorage {
  override def store(url: TCanonicalUrl, d: String) = {
    println("The URL " + url.canonize + " has the following text ")
    println(d.trim())
  }
}

class FileStorage extends TStorage {
  val dir = "storage/"
  def header(url:TCanonicalUrl):String = {
    var h = "Url:  " + url.canonize + "\nHostname : " + url.hostname + "\n"
    h +=  "--------------------------------------------\n"
    h
  }
  override def store(url: TCanonicalUrl, d: String): Unit = {
    val filename = dir + HashMD5.md5Hash(url.canonize)
    val writer = new PrintWriter(new File(filename))
    writer.write(header(url) + "\n" + d)
    writer.close()
  }
}