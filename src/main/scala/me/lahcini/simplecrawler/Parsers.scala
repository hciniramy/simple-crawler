import scala.util.matching.Regex

trait TParser {
  def extractFrom(data:String): List[String]
}

trait TUrlParser extends TParser


trait TUrlRegexParser extends TUrlParser {
  val url = new Regex("""(http(s?)\:\/\/|~/|/)?([a-zA-Z]{1}([\w\-]+\.)+([\w]{2,5}))(:[\d]{1,5})?/?(\w+\.[\w]{3,4})?((\?\w+=\w+)?(&\w+=\w+)*)?""")
  override def extractFrom(data: String):List[String] = {
    val urls = url.findAllIn(data).toList.map(u => { u.trim })
    urls
  }
}


class URLRegexParser extends TUrlRegexParser