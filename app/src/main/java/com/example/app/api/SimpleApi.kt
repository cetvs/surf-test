package com.example.app.api


import com.example.app.classes.Image
import com.example.app.classes.Movie
import com.example.app.classes.MoviesList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpleApi {
    @GET("3/movie/550?api_key=461062f3ca455541c4c57750fcbf6759")
    fun getMovie(): Call<Movie>

    @GET("3/movie/{key}/images?api_key=461062f3ca455541c4c57750fcbf6759")
    fun getImage(@Path("key") movieId : Int ):Call<Image>

    @GET("3/movie/{key}?api_key=461062f3ca455541c4c57750fcbf6759")
    fun getMovie(@Path("key") movieId : Int ): Call<Movie>

    @GET("3/movie/550?api_key=461062f3ca455541c4c57750fcbf6759")
    fun getMovies(): Call<Movie>

    @GET("3/search/movie?api_key=461062f3ca455541c4c57750fcbf6759")
    fun getMovieByName(@Query("query") query : String ): Call<MoviesList>

    @GET("3/movie/popular?api_key=461062f3ca455541c4c57750fcbf6759")
    fun getPopularMovie(): Call<MoviesList>
}