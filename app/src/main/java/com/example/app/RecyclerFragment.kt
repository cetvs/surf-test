package com.example.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.MyRecyclerAdapter
import com.example.app.adapters.OnItemClickListener

class RecyclerFragment : Fragment(), OnItemClickListener {
    lateinit var recyclerView : RecyclerView
    private lateinit var mContext: Context
    lateinit var myRecyclerAdapter : MyRecyclerAdapter;

    companion object{
        fun getNewInstance(args: MyRecyclerAdapter): RecyclerFragment{
            val recyclerFragment = RecyclerFragment()
            recyclerFragment.arguments?.putSerializable("recyclerAdapter", args)
            return recyclerFragment
        }

        fun getNewInstance(args: Bundle): RecyclerFragment{
            val recyclerFragment = RecyclerFragment()
            recyclerFragment.arguments = args
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
        //Toast.makeText(activity, "1111", Toast.LENGTH_SHORT).show()
        val view = inflater.inflate(R.layout.recycler_layout, container, false)
        recyclerView = view.findViewById(R.id.rv_movie)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        var myRecyclerAdapter =  arguments?.get("bRecyclerAdapter") as MyRecyclerAdapter?
        this.myRecyclerAdapter = myRecyclerAdapter!!;
        this.myRecyclerAdapter!!.setMyListener(this)
        recyclerView.adapter = this.myRecyclerAdapter;
        return view
    }

    override fun onItemClick(position: Int) {
        //Toast.makeText(activity, "1111", Toast.LENGTH_SHORT).show()
        Toast.makeText(activity, "${myRecyclerAdapter.getItem(position).toString()}", Toast.LENGTH_SHORT).show()
    }
}