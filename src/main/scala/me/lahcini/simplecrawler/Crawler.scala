import java.io.{File, FileWriter}

/**
  * Created by lahcini on 15.05.17.
  */
object Crawler extends App{
  def run = {
    val crawler = new SimpleCrawler(new SimpleDownloader, new FileStorage, new URLRegexParser, new SimpleScheduler, new RandomSelectionQueue)
    println("Starting the Crawler") ;
    crawler.init
    crawler.crawl
  }
  run
}

trait TCrawler {
  def init : Unit
  def crawl: Unit
}

trait TSimpleCrawler extends TCrawler{
  def logNewUrls(url:TCanonicalUrl, newUrls:List[String]) = {
    val dir = "logs/parsedUrls/"
    val filename = dir + HashMD5.md5Hash(url.canonize)
    val writer = new FileWriter(new File(filename), true)
    writer.write("FROM : " + url.canonize + "\n")
    newUrls.foreach(u => {
      writer.write(u + "\n")
    })
    writer.close
  }
  override def crawl: Unit = {
    var counter = 0 ;
    while(!scheduler.isEmpty && counter < 1000) {
      val url = scheduler.next
      if(!downloader.visited(url)){
        println("#" + counter + ";scheduled url " + url.canonize)
        val d = downloader.download(url)
        val newUrls = urlParser.extractFrom(d)
        logNewUrls(url, newUrls)
        newUrls.foreach(u => {
          queue.enqueue(CanonicalURL.convert(u, url))
        }
        )
        if(!d.equals("ERROR"))
          storage.store(url, d)
      }
      if(!queue.isEmpty)
        scheduler.schedule(queue.get)
      counter += 1
    }
  }
  def downloader: TDownloader
  def storage: TStorage
  def urlParser: TUrlParser
  def scheduler: TScheduler
  def queue: TQueue
}

class SimpleCrawler(_downloader: TDownloader, _storage: TStorage, _urlParser: TUrlParser, _scheduler: TScheduler, _queue: TQueue) extends TSimpleCrawler {
  override def init = {
    // Enqueue initial URLs in the queue
    queue.enqueue("http://en.wikipedia.org/wiki/D3.js")
    queue.enqueue("http://www.algo.informatik.tu-darmstadt.de/lehre/wintersemester-1314/effiziente-graphenalgorithmen/")
    queue.enqueue("http://swift.org/")
    // schedule a starting number of URLs
    for(i <- 1 to 3){
      if(!queue.isEmpty)
        scheduler.schedule(queue.get)
    }
  }
  override def downloader: TDownloader = _downloader
  override def storage: TStorage = _storage
  override def urlParser: TUrlParser = _urlParser
  override def scheduler: TScheduler = _scheduler
  override def queue: TQueue = _queue
}
