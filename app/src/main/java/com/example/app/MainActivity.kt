package com.example.app

//import android.R


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.adapters.MyRecyclerAdapter
import com.example.app.adapters.OnItemClickListener
import com.example.app.api.Constants
import com.example.app.api.SimpleApi
import com.example.app.classes.Movie
import com.example.app.classes.MoviesList
import com.example.app.fragments.EmptyFragment
import com.example.app.fragments.OutInternetFragment
import com.example.app.fragments.RecyclerFragment
import com.example.app.room.AppDatabase
import com.example.app.room.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, OnItemClickListener {
    private lateinit var myRecyclerAdapter: MyRecyclerAdapter
    lateinit var simpleApi: SimpleApi
    lateinit var movieViewModel: MovieViewModel
    var refreshString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieViewModel.readAll.observe(this, Observer {
            if (it != null) {
                if (it.isNotEmpty()) {
                    myRecyclerAdapter.setData(ArrayList(it))

                    var bundle = Bundle()
                    bundle.putSerializable("bRecyclerAdapter", myRecyclerAdapter)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.place_holder, RecyclerFragment.getNewInstance(bundle), "recyclerFragment")
                            .commit()
                }
                else
                {
                    var bundle = Bundle()
                    bundle.putString("bQueryString", refreshString)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.place_holder, EmptyFragment.getNewInstance(bundle))
                            .commit()
                }
            }
            else
            {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.place_holder, OutInternetFragment())
                        .commit()
            }
        })

        movieViewModel.getPopular()

        var retrofit : Retrofit=Retrofit.Builder().baseUrl(Constants.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

        simpleApi = retrofit.create(SimpleApi::class.java)

        myRecyclerAdapter = MyRecyclerAdapter(this, ArrayList<Movie>(), this)

        var swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipe_container)

        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            movieViewModel.makeSearch(refreshString)
            swipeContainer.setRefreshing(false)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        refreshString = query!!
        movieViewModel.makeSearch(refreshString)
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        refreshString = query!!
        movieViewModel.makeSearch(refreshString)
        return true
    }

    override fun onItemClick( position: Int) {
        Toast.makeText(this, "${myRecyclerAdapter!!.getItem(position).toString()}", Toast.LENGTH_SHORT).show()
    }

    override fun onImageClick(movie: Movie, favImageView: ImageView) {
        if (movie.favorite == false) {
            movie.favorite = true
            movieViewModel.addMovie(movie)
            favImageView?.setImageResource(R.drawable.fav)
        }
        else {
            movieViewModel.deleteMovie(movie.id)
            movie.favorite = false
            favImageView?.setImageResource(R.drawable.unfav)
        }
    }
}