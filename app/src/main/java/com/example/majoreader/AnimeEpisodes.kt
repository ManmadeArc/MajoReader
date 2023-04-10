package com.example.majoreader

import java.util.ArrayList


class AnimeEpisodes {
    var epiList: ArrayList<Pair<String, String>>
    var image: String? = null
    var synopsis: String? = null
    var title: String? = null

    constructor(
        arg1: String?,
        arg2: String?,
        arg3: ArrayList<kotlin.Pair<String, String>>,
        arg4: String?
    ) {
        title = arg1
        synopsis = arg2
        epiList = arg3
        image = arg4

    }
}