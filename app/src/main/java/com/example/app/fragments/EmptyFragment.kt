package com.example.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.classes.Movie

class EmptyFragment : Fragment(){
    private lateinit var mContext: Context
    // lateinit var myRecyclerAdapter: MyRecyclerAdapter
    companion object{
        fun getNewInstance(args: Bundle): EmptyFragment {
            val emptyFragment = EmptyFragment()
            emptyFragment.arguments = args
            return emptyFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.empty_search, container, false)
        var textView = view.findViewById<TextView>(R.id.text_view)
        textView.setText("По запросу ${arguments?.get("bQueryString")} ничего не найдено")
        return view
    }


}