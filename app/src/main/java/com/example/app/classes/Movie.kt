package com.example.app.classes

import com.example.app.R
import com.google.gson.annotations.SerializedName

class Movie  {
    var id: Int
    @SerializedName("title")
    var name: String? = null
    @SerializedName("overview")
    var description: String? = null
    var img: Int?  = R.drawable.test
    var favorite : Boolean = false
    var poster_path: String? = null

    override fun toString(): String {
        return name!!
    }
    constructor(id: Int, name: String?, description: String?,
                img: Int?,
                favorite : Boolean = false)
    {
        this.id  = id
        this.name = name
        this.description = description
        this.img = img
        this.favorite = favorite
    }

    constructor(id: Int, name: String?, description: String?,
                poster_path: String?,
                favorite : Boolean = false)
    {
        this.id  = id
        this.name = name
        this.description = description
        this.poster_path = poster_path
        this.favorite = favorite
    }

//    fun setImage(img: Int?){
//        this.img =  img
//    }
}