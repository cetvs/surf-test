package com.example.app.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.app.R
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
class Movie  {
    @PrimaryKey var id: Int
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