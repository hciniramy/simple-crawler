/*
* scala-uri library to manipulate URIs
* see : https://github.com/lemonlabsuk/scala-uri
* */
import com.netaporter.uri.Uri
import com.netaporter.uri.dsl._

/*
* URL trait
* Used to represent a canonical URL
* */
trait TCanonicalUrl {
  def protocol : String
  def hostname : String
  def path : String
  def port : Int
  def canonize:String = {
    protocol + "://" + hostname + ":" + port + path
  }
}

class CanonicalURL(_pr: String, _h: String, _pa : String, _po: Int) extends TCanonicalUrl {
  override def protocol: String = "http"
  override def hostname: String = _h
  override def path: String = _pa
  override def port: Int = _po
}
object CanonicalURL {
  val defaultProtocol = "http" ;
  val defaultHost = "www.tu-darmstadt.de"
  val defaultPath = ""
  val defaultPort = 80

  def apply(url: String):CanonicalURL = {
    val uri: Uri = url
    new CanonicalURL(uri.protocol.getOrElse(defaultProtocol), uri.host.getOrElse(defaultHost), uri.path, uri.port.getOrElse(defaultPort))
  }

  def convert(u:String, url:TCanonicalUrl):String = {
    if(!u.contains("//")){
      if(u.charAt(0).equals('/')) {
        // relative url
        "http://" + url.hostname + ":" + url.port + u
      }else{
        "http://" + url.hostname + ":" + url.port + "/" + u
      }
    }else{
      u
    }
  }
}