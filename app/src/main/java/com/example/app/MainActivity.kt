package com.example.app

//import android.R


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.adapters.MyRecyclerAdapter
import com.example.app.api.Constants
import com.example.app.api.SimpleApi
import com.example.app.classes.Movie
import com.example.app.classes.MoviesList
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
    var refreshString = ""

    fun emptyQuery( simpleApi: SimpleApi)
    {
        for (i in 505..525) {
            var call: Call<Movie> = simpleApi.getMovie(i)
            //var imageCall: Call<Image> = simpleApi.getImage(i)
//            lateinit var movie :Movie

            call.enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.v("retrofit", "call failed")
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    var lst = myRecyclerAdapter.getList()
                    if (response.isSuccessful()) {
                        var movie = Movie(response.body()!!.id, response.body()!!.name,
                                response.body()!!.description,
                                response.body()!!.poster_path)
                        lst.add(movie)
                        myRecyclerAdapter.notifyItemInserted(lst.size - 1 )
                    }
                }
            })
        }
    }

    fun getPopular(simpleApi: SimpleApi) {
        var call: Call<MoviesList> = simpleApi.getPopularMovie()
        movieAsyncCall(call)
    }

    fun searchMovie(simpleApi: SimpleApi, str : String ) {
        if (str =="" || str ==" ")
            getPopular(simpleApi)
        else {
            var call: Call<MoviesList> = simpleApi.getMovieByName(str)
            movieAsyncCall(call)
        }
    }

    fun movieAsyncCall( call : Call<MoviesList> ) {
        call.enqueue(object : Callback<MoviesList> {

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {

            }

            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                var list = ArrayList<Movie>()
                myRecyclerAdapter.setData(list)

                var movies = response.body()!!.results
                for (i in 0..movies!!.size - 1) {
                    var movie = Movie(movies[i].id, movies[i].name,
                            movies[i].description,
                            movies[i].poster_path)
                    list.add(movie)

                    myRecyclerAdapter.notifyItemInserted(list.size - 1)
                }
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var retrofit : Retrofit=Retrofit.Builder().baseUrl(Constants.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

        simpleApi= retrofit.create(SimpleApi::class.java)
        getPopular(simpleApi)


        myRecyclerAdapter = MyRecyclerAdapter(this, arrayListOf())
        recyclerView = findViewById<RecyclerView>(R.id.rv_movie)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myRecyclerAdapter

        var swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            searchMovie(simpleApi, refreshString)
            swipeContainer.setRefreshing(false)
        })

//        var fTrans = getFragmentManager().beginTransaction();
//        fTrans.add()

        var txt = findViewById<TextView>(R.id.textView)
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