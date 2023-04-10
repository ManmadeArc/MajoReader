package com.example.majoreader
import java.util.*

class MangaFullData(
    var title: String,
    var imageUrl: String,
    var synopsis: String,
    var epiList: ArrayList<ChapterInfo>
){
    constructor() : this("","","", ArrayList())
}