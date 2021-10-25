package com.example.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.api.Constants.Companion.Base_URL_Image
import com.example.app.classes.Movie
import java.io.Serializable


//make MutableList
class MyRecyclerAdapter(private val context: Context, private var list: ArrayList<Movie>)
    : Serializable, RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_layout, parent, false)
        return MyRecyclerHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

//    fun addWithUpdate(movie : Movie)
//    {
//        list.add(movie)
//        notifyItemInserted(list.size - 1)
//    }

    fun getList() : ArrayList<Movie>
    {
        return list
    }

    fun setData(lst: ArrayList<Movie>)
    {
        list = lst
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyRecyclerHolder, position: Int) {
        val fragment: Movie = list[position]
        holder.bind(fragment)

    }


    inner class MyRecyclerHolder(itemView: View):
            RecyclerView.ViewHolder(itemView)
    {

        private var descriptionView: TextView?= null
        private var nameView: TextView?= null
        private var imageView: ImageView?= null
        private var favImageView: ImageView?= null

        init {
            descriptionView = itemView.findViewById(R.id.tv_description)
            nameView = itemView.findViewById(R.id.tv_name)
            imageView = itemView.findViewById(R.id.iv_poster)
            favImageView = itemView.findViewById(R.id.iv_fav)
        }

        fun bind(movie: Movie) {
            if(movie.favorite)
                favImageView?.setImageResource(R.drawable.fav)
            else
                favImageView?.setImageResource(R.drawable.unfav)
            //imageView?.setImageResource(movie.img!!)
            Glide.with(context)
                    .load(Base_URL_Image + movie.poster_path)
                    .into(imageView!!)
            descriptionView?.text = movie.description
            nameView?.text = movie.name
        }
    }
}