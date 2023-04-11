package com.example.majoreader

import java.util.ArrayList


class AnimeEpisodes (
    var title: String? = null,
    var synopsis: String? = null,
    var epiList: ArrayList<Pair<String, String>>,
    var image: String? = null,


){

    constructor(
    ) : this("","",ArrayList(),"")
}