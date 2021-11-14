package com.example.app.adapters

import android.view.View
import android.widget.ImageView
import com.example.app.classes.Movie

interface OnItemClickListener{
    fun onItemClick(position: Int)
    fun onImageClick(movie: Movie, favImageView: ImageView)
}