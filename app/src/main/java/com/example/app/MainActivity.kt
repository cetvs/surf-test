package com.example.app

//import android.R


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.adapters.MyRecyclerAdapter
import com.example.app.adapters.OnItemClickListener
import com.example.app.api.Constants
import com.example.app.api.SimpleApi
import com.example.app.classes.Movie
import com.example.app.classes.MoviesList
import com.example.app.room.AppDatabase
import com.example.app.room.MovieDao
import com.example.app.room.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var myRecyclerAdapter: MyRecyclerAdapter
    //var lst = arrayListOf<Movie>()
    lateinit var recyclerView: RecyclerView
    lateinit var simpleApi: SimpleApi
    lateinit var movieRepository : MovieRepository
    var refreshString = ""

    fun getPopular(simpleApi: SimpleApi) {
        var call: Call<MoviesList> = simpleApi.getPopularMovie()
        movieAsyncCall(call,"")
    }

    fun searchMovie(simpleApi: SimpleApi, str : String ) {
        if (str =="" || str ==" ")
            getPopular(simpleApi)
        else {
            var call: Call<MoviesList> = simpleApi.getMovieByName(str)
            movieAsyncCall(call, str)
        }
    }

    fun movieAsyncCall( call : Call<MoviesList>, str : String ) {
        call.enqueue(object : Callback<MoviesList> {
            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.place_holder, OutInternetFragment())
                    .commit()
            }

            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                var list = ArrayList<Movie>()
                myRecyclerAdapter.setData(list)
                var movies = response.body()!!.results

                if (movies!!.size == 0) {
                    var bundle = Bundle()
                    bundle.putString("bQueryString", str)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.place_holder, EmptyFragment.getNewInstance(bundle))
                        .commit()
                }
                else {
                    var bundle = Bundle()
                    bundle.putSerializable("bRecyclerAdapter", myRecyclerAdapter)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.place_holder, RecyclerFragment.getNewInstance(bundle), "recyclerFragment")
                            .commit()

                    for (i in 0..movies.size - 1) {
                        var movie = Movie(movies[i].id, movies[i].name,
                                movies[i].description,
                                movies[i].poster_path
                        )
                        list.add(movie)
                        myRecyclerAdapter.notifyItemInserted(list.size - 1)
                    }
                }
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show()

        var retrofit : Retrofit=Retrofit.Builder().baseUrl(Constants.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
        simpleApi= retrofit.create(SimpleApi::class.java)

        //var movie = Movie(1,"test","test", "");
        myRecyclerAdapter = MyRecyclerAdapter(this, ArrayList<Movie>())

        //Create repository
        val noteDao = AppDatabase.getDatabase(application).movieDao()
        movieRepository = MovieRepository(noteDao)

        getPopular(simpleApi)

        var bundle = Bundle()
        bundle.putSerializable("bRecyclerAdapter", myRecyclerAdapter)
        var recyclerFragment = RecyclerFragment.getNewInstance(bundle)

        supportFragmentManager.beginTransaction()
                .add(R.id.place_holder, recyclerFragment, "recyclerFragment")
                .commit()

        var swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipe_container)

        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            searchMovie(simpleApi, refreshString)
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
        searchMovie(simpleApi, refreshString)
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        refreshString = query!!
        searchMovie(simpleApi, refreshString)
        return true
    }


}