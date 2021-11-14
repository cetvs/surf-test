package com.example.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.classes.Movie

class OutInternetFragment : Fragment(){
    private lateinit var mContext: Context
    // lateinit var myRecyclerAdapter: MyRecyclerAdapter
    companion object{
        fun getNewInstance(args: Bundle): OutInternetFragment {
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