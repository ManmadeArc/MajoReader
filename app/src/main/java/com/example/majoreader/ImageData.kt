package com.example.majoreader

import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics

class ImageData(img: ArrayList<String>, reference: String) {
    var images: ArrayList<String>
    var referer: String

    init {
        images = img
        referer = reference
    }
}