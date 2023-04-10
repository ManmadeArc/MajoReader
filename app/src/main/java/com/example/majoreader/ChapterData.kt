package com.example.majoreader


class ChapterData {
    var chapterId: String
    var chapterTitle: String
    var imageUrl: String

    constructor(title: String, imageUrl: String, id: String){
        chapterTitle = title
        this.imageUrl = imageUrl
        chapterId = id
    }

}