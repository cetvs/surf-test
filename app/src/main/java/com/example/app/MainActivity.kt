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
    lateinit var recyclerView: RecyclerView
    lateinit var simpleApi: SimpleApi
    lateinit var movieRepository : MovieRepository
    lateinit var movieViewModel: MovieViewModel

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
                        var movie: Movie? = null

                        movie = Movie(movies[i].id, movies[i].name,
                                movies[i].description,
                                movies[i].poster_path)

                        list.add(movie!!)
//                        lifecycleScope.launch(Dispatchers.IO) {
//                            if (movieRepository.findMovie(movie.id) != null)
                                //movie.favorite = true
//                            list.add(movie!!)
                        myRecyclerAdapter.notifyItemInserted(i)
//                        }
                    }
                }
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        var retrofit : Retrofit=Retrofit.Builder().baseUrl(Constants.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

        simpleApi= retrofit.create(SimpleApi::class.java)

        //var movie = Movie(1,"test","test", "");
        myRecyclerAdapter = MyRecyclerAdapter(this, ArrayList<Movie>(), this)


        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                var movie = movieRepository.findMovie(580489)
                if (movie != null) {
                    var str = movie.toString()
                    Log.v(str, "1")
                }
            }
        }

        //Create repository
        val noteDao = AppDatabase.getDatabase(this).movieDao()
        movieRepository = MovieRepository(noteDao)
        lifecycleScope.launch(Dispatchers.IO) {
            movieRepository.deleteMovies()
        }
        getPopular(simpleApi)

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

    override fun onItemClick( position: Int) {
        Toast.makeText(this, "${myRecyclerAdapter!!.getItem(position).toString()}", Toast.LENGTH_SHORT).show()
    }

    override fun onImageClick(movie: Movie, favImageView: ImageView) {
        if (movie.favorite == false) {
            movie.favorite = true
            lifecycleScope.launch(Dispatchers.IO) {
                movieRepository.addMovie(movie)
            }
            favImageView?.setImageResource(R.drawable.fav)
        }
        else {
            lifecycleScope.launch(Dispatchers.IO) {
                movieRepository.deleteMovie(movie.id)
            }
            movie.favorite = false
            favImageView?.setImageResource(R.drawable.unfav)
        }
    }


}