package com.example.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.MyRecyclerAdapter

class RecyclerFragment : Fragment(){
    lateinit var recyclerView : RecyclerView
    private lateinit var mContext: Context
    private var myRecyclerAdapter : MyRecyclerAdapter? = null;

    companion object{
        fun getNewInstance(args: MyRecyclerAdapter): RecyclerFragment {
            val recyclerFragment = RecyclerFragment()
            recyclerFragment.arguments?.putSerializable("recyclerAdapter", args)
            return recyclerFragment
        }

        fun getNewInstance(args: Bundle): RecyclerFragment {
            val recyclerFragment = RecyclerFragment()
            recyclerFragment.arguments = args
            return recyclerFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.recycler_layout, container, false)
        recyclerView = view.findViewById(R.id.rv_movie)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        var myRecyclerAdapter =  arguments?.getSerializable("bRecyclerAdapter") as MyRecyclerAdapter?
        this.myRecyclerAdapter = myRecyclerAdapter

        recyclerView.adapter = this.myRecyclerAdapter;
        return view
    }

}