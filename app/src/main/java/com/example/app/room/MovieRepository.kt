package com.example.app.room

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app.R
import com.example.app.api.Constants
import com.example.app.api.SimpleApi
import com.example.app.classes.Movie
import com.example.app.classes.MoviesList
import com.example.app.fragments.EmptyFragment
import com.example.app.fragments.OutInternetFragment
import com.example.app.fragments.RecyclerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MovieRepository(private val localDataSource: MovieDao) {
    private val simpleApi: SimpleApi
//    private val remoteDataSource: MovieRemoteDataSource


    init {
        var retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        simpleApi = retrofit.create(SimpleApi::class.java)
    }

    fun addMovie(el: Movie) {
        localDataSource.insertMovie(el)
    }

    fun addMovies(lst: List<Movie>) {
        for (elem in lst)
            localDataSource.insertMovie(elem);
    }

    fun deleteMovies() {
        return localDataSource.deleteAll()
    }

    fun deleteMovie(id: Int) {
        return localDataSource.delete(id)
    }

    fun findMovie(id: Int): Movie? {
        return localDataSource.findMovie(id)
    }

    fun getAll(): LiveData<List<Movie>> {
        return localDataSource.getAll()
    }


    fun getPopular(liveData: MutableLiveData<List<Movie>>){
        var call: Call<MoviesList> = simpleApi.getPopularMovie()
        movieAsyncCall(call,  liveData)
    }

    fun movieAsyncCall(call: Call<MoviesList>, liveData: MutableLiveData<List<Movie>>,  str: String = ""){
        call.enqueue(object : Callback<MoviesList> {
            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                liveData.value = null
            }

            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                var movies = response.body()!!.results

                if (movies == null) {
                    liveData.value = null
                }
                else {
                    if(movies.isEmpty())
                        liveData.value = ArrayList()
                    else
                        GlobalScope.launch {
                            liveData.postValue(checkMoviesInDb(movies))
                        }
                }
            }
        })
    }

    private fun checkMoviesInDb(lst: List<Movie>): ArrayList<Movie> {
            var resLst = ArrayList<Movie>()
            for (i in 0..lst.size - 1) {
                var movie = localDataSource.findMovie(lst[i].id)

                if (movie != null)
                    resLst.add(movie)
                else
                    resLst.add(lst[i])
            }
        return resLst
    }

    fun makeSearch(liveData: MutableLiveData<List<Movie>>, str: String) {
        if (str =="" || str ==" ")
            getPopular(liveData)
        else {
            var call: Call<MoviesList> = simpleApi.getMovieByName(str)
            movieAsyncCall(call, liveData,str)
        }
    }
}