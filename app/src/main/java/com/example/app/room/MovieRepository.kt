package com.example.app.room

import androidx.lifecycle.LiveData
import com.example.app.classes.Movie


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

    fun deleteMovie(id: Int) {
        return movieDao.delete(id)
    }

//    fun isFindMovie(id: Int): Boolean{
//        return movieDao.IsfindMovie(id)
//    }

    fun findMovie(id: Int): Movie?{
        return movieDao.findMovie(id)
    }


//    fun findMovie(string: String): LiveData<List<Movie>>{//List<Movie>{
//        return movieDao.findMovie(string)
//    }

    fun getAll(): LiveData<List<Movie>>{
        return movieDao.getAll()
    }

//    fun search(searchQuery : String) : LiveData<List<Movie>>{
//        return movieDao.getSearchResults(searchQuery)
//    }




}