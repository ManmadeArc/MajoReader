package com.example.majoreader



import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.ArrayList

class TMOScrapper {
    private var cachedImages: ImageData? = null
    private var cachedMangaData: MangaFullData? = null
    private var cachedMatches: ArrayList<MangaData>? = null
    private  var cachedPopular: ArrayList<MangaData>? = null
    private fun getCachedPopular(): ArrayList<MangaData>? {
        val arrayList: ArrayList<MangaData> = cachedPopular!!
        return arrayList
    }

    fun setCachedPopular(popular: ArrayList<MangaData>?) {
        cachedPopular = popular
    }

    fun getCachedMangaData(): MangaFullData? {
        return cachedMangaData
    }

    fun setCachedMangaData(data: MangaFullData?) {
        cachedMangaData = data
    }

    fun getCachedImages(): ImageData? {
        return cachedImages
    }

    fun setCachedImages(img: ImageData?) {
        cachedImages = cachedImages
    }

    fun getCachedMatches(): ArrayList<MangaData>? {
        val arrayList: ArrayList<MangaData>? = cachedMatches
        return arrayList
    }

    fun setCachedMatches(Mangas: ArrayList<MangaData>?) {
        cachedMatches = Mangas
    }

    fun getLatestPopular(): ArrayList<MangaData>?  {
            var str: String
            var arrayList: ArrayList<MangaData>?
            cachedPopular = ArrayList()
            var doc = Jsoup.connect(BaseSite).userAgent(UserAgent).get()
            val it: Iterator<*> = doc.select("#pills-populars > div:nth-child(1) > div").iterator()
            while (true) {
                str = "cachedPopular"
                if (!it.hasNext()) {
                    break
                }
                val element = it.next() as Element
                val str2 = "title"
                var attr = element.select("a > div > div > h4").attr(str2)

                val title = attr
                attr = element.select("a > div > style").toString()
                val text = attr
                val regex =
                    Regex("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")

                val doc2 = doc
                val poster = regex.find(text).toString()
                var attr2 = element.select("a").attr("href")

                val id = attr2
                attr2 = element.select("a > div >span:nth-child(5)").text()

                val genre = attr2
                arrayList = cachedPopular
                arrayList!!.add(MangaData(title, poster, id, genre))
                doc = doc2
            }
            arrayList = cachedPopular
            return arrayList
        }

    fun getEpisodeList(id: String?): MangaFullData? {
        var str: String
        var str2: String
        var str3: String
        var str4: String
        var str5: String?
        var str6: String
        var str7: String?
        var chapter: Element
        var name: String?
        var link: String?
        val epiList: ArrayList<ChapterInfo> = ArrayList<ChapterInfo>()
        var doc = Jsoup.connect(id).userAgent(UserAgent).get()
        val synopsis =
            doc.select("#app > section > header > section.element-header-content > div.container.h-100 > div > div.col-12.col-md-9.element-header-content-text > p.element-description")
                .text()
        val poster =
            doc.select("#app > section > header > section.element-header-content > div.container.h-100 > div > div.col-12.col-md-3.text-center > div > img")
                .attr("src")
        val title =
            doc.select("#app > section > header > section.element-header-content > div.container.h-100 > div > div.col-12.col-md-9.element-header-content-text > h2")
                .text()
        var chapters = doc.select("#chapters > ul > li")
        val extraChapters = doc.select("#chapters-collapsed > li")
        var it: Iterator<*> = chapters.iterator()
        while (true) {
            val hasNext = it.hasNext()
            str = DataBase.TABLE_ROW_LINK
            str2 = DataBase.TABLE_ROW_NAME
            str3 = "chapter.select(\"div > di…-right > a\").attr(\"href\")"
            str4 = "href"
            str5 = "div > div > ul > li > div > div.col-2.col-sm-1.text-right > a"
            val doc2 = doc
            str6 = "chapter.select(\"h4 > div…xt-truncate > a \").text()"
            val chapters2 = chapters
            str7 = "h4 > div > div.col-10.text-truncate > a "
            if (!hasNext) {
                break
            }
            chapter = it.next() as Element
            str7 = chapter.select(str7).text()

            name = str7
            str5 = chapter.select(str5).attr(str4)

            link = str5

            epiList.add(ChapterInfo(name, link))
            str5 = id
            doc = doc2
            chapters = chapters2
        }
        it = extraChapters.iterator()
        while (it.hasNext()) {
            chapter = it.next() as Element
            val str8 = str7
            str7 = chapter.select(str7).text()

            name = str7
            str7 = chapter.select(str5).attr(str4)

            link = str7
            epiList.add(ChapterInfo(name, link))
            str7 = str8
        }
        val mangaFullData = MangaFullData(title, poster, synopsis, epiList)
        cachedMangaData = mangaFullData

        return mangaFullData
    }

    fun getChapterImages(chapter: String?): ImageData? {

        val imgs: ArrayList<String> = ArrayList<String>()
        val connect = Jsoup.connect(chapter)
        val str = UserAgent
        var text =
            connect.userAgent(str).get().select("#app > header > nav > div > div:nth-child(4) > a")
                .attr("href")
        val regex = Regex("paginated")

        if (regex.containsMatchIn(text)) {
            text = chapter
        }
        val it: Iterator<*> =
            Jsoup.connect(text).userAgent(str).get().select("#main-container > div ").iterator()
        while (it.hasNext()) {
            imgs.add((it.next() as Element).select("img").attr("data-src"))
        }
        val imageData = ImageData(imgs, text)
        cachedImages = imageData

        return imageData
    }

    fun search(query: String, page: Int): ArrayList<MangaData>? {

        var str2 = query
        var text: String = ""
        var poster: String = ""

        var queries = ArrayList<Pair<String, String>>()
        queries.add(  Pair("_pg", page.toString()))
        val str3 = "title"
        queries.add(  Pair(str3, str2))
        var queries2 = queries.toMap()
        cachedMatches = ArrayList<MangaData>()
        val it: Iterator<*> =
            Jsoup.connect("https://lectortmo.com/library").data(queries2)
                .userAgent(UserAgent).get()
                .select("#app > main > div:nth-child(2) > div.col-12.col-lg-8.col-xl-9 > div:nth-child(3) > div")
                .iterator()
        while (true) {
            if (!it.hasNext()) {
                break
            }

            val result = it.next() as Element
            val noElementsDiv = result.selectFirst("div:contains(No hay elementos)")
            val breakCondition = noElementsDiv != null
            if( breakCondition) break
            var attr = result.select("a > div > div > h4").attr(str3)

            val title = attr
            attr = result.select("a > div > style").toString()

            text = attr
            val regex =
                Regex("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
            Log.i("INFO",result.toString())
            poster = regex.findAll(text, 0).elementAt(0).value
            attr = result.select("a").attr("href")

            val id = attr
            attr = result.select("a > div > span:nth-child(5)").attr(str3)

            val genre = attr
            val arrayList: ArrayList<MangaData>? = cachedMatches
            arrayList!!.add(MangaData(title, poster, id, genre))
        }
        return cachedMatches
    }

    companion object {
        private const val BaseSite = "https://lectortmo.com"
        private const val UserAgent = "Mozilla/5.0"
        private const val request = "/library"

    }
}