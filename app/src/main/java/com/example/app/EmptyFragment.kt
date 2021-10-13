package com.example.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.adapters.MyRecyclerAdapter
import com.example.app.classes.Movie

class EmptyFragment : Fragment(){
    private lateinit var mContext: Context
    private lateinit var myRecyclerAdapter: MyRecyclerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var lst = arrayListOf<Movie>()
        myRecyclerAdapter = MyRecyclerAdapter(mContext, lst)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.empty_search, container, false)
    }


}