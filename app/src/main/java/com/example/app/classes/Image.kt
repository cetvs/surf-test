package com.example.app.classes

import com.google.gson.annotations.SerializedName

class Image {
    var id: Int? = null
    @SerializedName("file_path")
    var path: String? = null

}