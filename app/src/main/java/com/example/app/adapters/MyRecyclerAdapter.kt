package com.example.app.adapters

import android.content.Context
import android.util.Log
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

class MyRecyclerAdapter(private val context: Context, private var list: ArrayList<Movie>,
                        var listener: OnItemClickListener? = null)
    : Serializable, RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_layout, parent, false)
        return MyRecyclerHolder(itemView)
    }

    override fun getItemCount(): Int = list.size


    fun getItem(index: Int) : Movie
    {
        return list[index]
    }

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
        val movie: Movie = list[position]
        holder.bind(movie)
    }


    inner class MyRecyclerHolder(itemView: View):
            RecyclerView.ViewHolder(itemView), View.OnClickListener
    {

        private var descriptionView: TextView?= null
        private var nameView: TextView?= null
        private var imageView: ImageView?= null
        private var favImageView: ImageView?= null

        init {
            itemView.setOnClickListener(this)
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

            favImageView?.setOnClickListener {
                listener!!.onImageClick(movie, favImageView!!)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
//            if (v?.getId() == R.id.imageView)
//            {
//                favImageView?.setImageResource(R.drawable.fav)
            if(position != RecyclerView.NO_POSITION){
                listener!!.onItemClick( position)
            }
        }
    }




}