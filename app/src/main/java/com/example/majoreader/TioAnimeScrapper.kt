package com.example.majoreader

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import java.lang.StringBuilder
import java.util.*

class TioAnimeScrapper {
    private var cachedRecent: ArrayList<ChapterData>? = null

    fun getCachedRecent(): ArrayList<ChapterData>? {

        return this.cachedRecent
    }

    fun setCachedRecent(chapters: ArrayList<ChapterData>?) {
        cachedRecent = chapters
    }

    val latestEpisodes: ArrayList<ChapterData>?
        get() {
            var str: String
            cachedRecent = ArrayList()
            val str2 = BaseSite
            val it = Jsoup.connect(str2).get().select("#tioanime > div > section:nth-child(1) > ul > li")
                    .iterator()
            while (true) {
                str = "cachedRecent"
                if (!it.hasNext()) {
                    break
                }
                val post: Element = it.next() as Element
                var text: String = post.select("article > a > h3").text()

                val title = text
                val stringBuilder = StringBuilder()
                stringBuilder.append(str2)
                stringBuilder.append(post.select("article > a > div > figure > img").attr("src"))
                val poster = stringBuilder.toString()
                text = post.select("article>a").attr("href")

                val id = text
                val arrayList: ArrayList<ChapterData>? = cachedRecent

                arrayList!!.add(ChapterData(title, poster, id))
            }
            val arrayList2: ArrayList<ChapterData>? = cachedRecent

            return arrayList2
        }

    fun getServers(id: String): ArrayList<Server> {
        var element: String

        val servers: ArrayList<Server> = ArrayList()
        val stringBuilder = BaseSite + id
        var query: String = ""
        val it=Jsoup.connect(stringBuilder.toString()).get().select("body > script").iterator()
        while (it.hasNext()) {
            element = (it.next() as Element).toString()

            val text = element
            val regex = Regex("var videos")

            if (regex.containsMatchIn(text)) {
                query = text
                break
            }
        }
        val matches = Regex("\"(.*?)\"").findAll(query,0).toList()
        val step = (0 until matches.count() step 2)
        var i = step.first
        val last = step.last
        val step2 = step.step
        if (if (step2 < 0) i < last else i > last) {
            while (true) {
                var str2 = "\""
                val str3 = ""
                val str1 = Regex(str2).replace(matches[i].value, str3)
                element = Regex(str2).replace(matches[i+1].value, str3)
                val regex2 = Regex("\\\\")
                element = regex2.replace(element, str3)
                servers.add(Server(str1, element))
                if (i == last) {
                    break
                }
                i += step2
            }
        }
        return servers
    }

    fun search(query: String?): ArrayList<SearchQuery> {

        val fullQuery = "https://tioanime.com/directorio?q=$query"
        val results: ArrayList<SearchQuery> = ArrayList<SearchQuery>()
        val it: Iterator<*> = Jsoup.connect(fullQuery).get()
            .select("#tioanime > div > div.row.justify-content-between.filters-cont > main > ul > li")
            .iterator()
        while (it.hasNext()) {
            val match: Element = it.next() as Element
            var text: String = match.select("article > a >h3").text()

            val title = text
            val stringBuilder = StringBuilder()
            stringBuilder.append(BaseSite)
            stringBuilder.append(
                match.select("article > a > div > figure > img").attr("src").toString()
            )
            val poster = stringBuilder.toString()
            text = match.select(" article > a").attr("href")

            val id = text

            results.add(SearchQuery(title, poster, id))
        }
        return results
    }

    fun getAnimeEpiList(id: String?): AnimeEpisodes {
        var element: String
        var str = id

        val stringBuilder = StringBuilder()
        var str2 = BaseSite
        stringBuilder.append(str2)
        stringBuilder.append(str)
        val subDoc: Document = Jsoup.connect(stringBuilder.toString()).get()
        val synopsis: String =
            subDoc.select("#tioanime > article > div > div > aside.col.col-sm-8.col-lg-9.col-xl-10 > p.sinopsis")
                .text()
        var eps: String = ""
        val it = subDoc.select("body > script").iterator()
        while (it.hasNext()) {
            val script: Element = it.next() as Element
            val regex = Regex("var anime_info")
            val element2: String = script.toString()
            if (regex.containsMatchIn(element2)) {
                element = script.toString()
                eps = element
            }
        }
        val title = subDoc.select("#tioanime > article > div > div > aside.col.col-sm-8.col-lg-9.col-xl-10 > h1").text()
        val arrays = Regex("\\[(.*?)\\]").findAll(eps, 0).toList()
        val image = subDoc.select("#tioanime > article > div > div > aside.col.col-sm-4.col-lg-3.col-xl-2 > div > figure > img").attr("src")
        element = Regex("\"").replace(arrays[0].groupValues[1], "")
        val episodes = Regex("\\d+").findAll(arrays[1].value).toList()
        val episodeList = ArrayList<Pair<String,String>>()

        for (i in episodes) {
            val value = i.value
            val episodeUrl = "/ver/$element-$value"
            episodeList.add(Pair<String,String>(value,episodeUrl))
            str = id
        }
        return AnimeEpisodes(title, synopsis, episodeList, image)
    }

    companion object {
        private const val BaseSite = "https://tioanime.com"
        private const val SearchDirectory = "/directorio?q="
    }
}