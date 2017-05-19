import java.io.{File, FileWriter}
import java.util.Calendar

import scala.collection.mutable.ListBuffer
import scalaj.http.{Http, HttpOptions, HttpRequest}

trait TDownloader {
  def download(url:TCanonicalUrl): String
  def visited(url:TCanonicalUrl):Boolean
}

trait TDownloaderMonitoring extends TDownloader {
  val dir = "logs/downloader"
  def log(url:TCanonicalUrl, respCode: Int)
  def logDuplicates(url:TCanonicalUrl)
}
class SimpleDownloader extends TDownloader with TDownloaderMonitoring {
  override def download(url:TCanonicalUrl):String = {
    val request: HttpRequest = Http(url.canonize).options(HttpOptions.allowUnsafeSSL, HttpOptions.followRedirects(true),HttpOptions.connTimeout(5000),
      HttpOptions.readTimeout(5000))
    val response = request.asString

    hashedURLs += HashMD5.md5Hash(url.canonize)
    log(url, response.code)
    if(response.isSuccess){
      response.body
    }else{
      "ERROR"
    }
  }

  val hashedURLs:ListBuffer[String] = new ListBuffer()
  override def visited(url: TCanonicalUrl): Boolean = {
    if(hashedURLs.contains(HashMD5.md5Hash(url.canonize)))
      logDuplicates(url)

    hashedURLs.contains(HashMD5.md5Hash(url.canonize))
  }

  override def log(url: TCanonicalUrl, respCode: Int): Unit = {
    val filename = dir
    val writer = new FileWriter(new File(filename), true)
    writer.write("#ID=" + HashMD5.md5Hash(url.canonize) + ";" + url.canonize + ";" + respCode + ";" + url.hostname + "\n")
    writer.close()
  }

  override def logDuplicates(url: TCanonicalUrl): Unit = {
    val filename = dir + "_duplicates"
    val writer = new FileWriter(new File(filename), true)
    writer.write("#ID=" + HashMD5.md5Hash(url.canonize) + ";" + url.canonize + "\n")
    writer.close()
  }
}