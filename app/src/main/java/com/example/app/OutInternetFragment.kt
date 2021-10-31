package com.example.app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.adapters.MyRecyclerAdapter
import com.example.app.classes.Movie

class OutInternetFragment : Fragment(){
    private lateinit var mContext: Context
    // lateinit var myRecyclerAdapter: MyRecyclerAdapter
    companion object{
        fun getNewInstance(args: Bundle): OutInternetFragment{
            val emptyFragment = OutInternetFragment()
            emptyFragment.arguments = args
            return emptyFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var lst = arrayListOf<Movie>()
        //myRecyclerAdapter = MyRecyclerAdapter(mContext, lst)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.out_internet_layout, container, false)
        return view
    }

}