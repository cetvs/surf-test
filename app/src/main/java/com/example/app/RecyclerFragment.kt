package com.example.app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.MyRecyclerAdapter
import java.io.Serializable

class RecyclerFragment : Fragment(){
    lateinit var recyclerView : RecyclerView
    private lateinit var mContext: Context
    lateinit var myRecycler : MyRecyclerAdapter;

    companion object{
        fun getNewInstance(args: MyRecyclerAdapter): RecyclerFragment{
            val recyclerFragment = RecyclerFragment()
            recyclerFragment.arguments?.putSerializable("recyclerAdapter", args)
            return recyclerFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.recycler_layout, container, false)
        recyclerView = view.findViewById(R.id.rv_movie)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        if (arguments?.getSerializable("recyclerAdapter") as MyRecyclerAdapter? == null)
//            Log.v("1", "null" )
//        else
//            Log.v("2", "not null" )
        //var recyclerAdapter : MyRecyclerAdapter = arguments?.getSerializable("recyclerAdapter") as MyRecyclerAdapter
        //Thread.sleep(1_000)
        if (arguments?.getSerializable("recyclerAdapter") == null)
            Log.e("1","1")
        else
            Log.e("2","2")
        recyclerView.adapter = arguments?.getSerializable("recyclerAdapter") as MyRecyclerAdapter?
        return view
    }
}