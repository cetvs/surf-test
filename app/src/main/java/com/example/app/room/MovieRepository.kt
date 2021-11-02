package com.example.app.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.app.api.SimpleApi
import com.example.app.classes.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository(private val movieDao: MovieDao) {

    suspend fun addMovie(el: Movie){
        movieDao.insertMovie(el)
    }

    suspend fun addMovies(lst: List<Movie>){
        for (elem in lst)
            movieDao.insertMovie(elem);
    }

    fun deleteMovies() {
        return movieDao.deleteAll()
    }

    fun getAll(): LiveData<List<Movie>>{
        return movieDao.getAll()
    }

    fun search(searchQuery : String) : LiveData<List<Movie>>{
        return movieDao.getSearchResults(searchQuery)
    }




}