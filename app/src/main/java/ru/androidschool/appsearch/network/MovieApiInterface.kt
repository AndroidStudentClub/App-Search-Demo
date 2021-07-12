package ru.androidschool.appsearch.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.androidschool.appsearch.data.Movie
import ru.androidschool.appsearch.data.ApiResponse
import ru.androidschool.appsearch.data.Person

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getPlayingNowMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = RU_LANGUAGE_CODE
    ): Observable<ApiResponse<Movie>>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = RU_LANGUAGE_CODE
    ): Observable<ApiResponse<Movie>>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = RU_LANGUAGE_CODE
    ): Observable<ApiResponse<Movie>>

    @GET("tv/popular")
    fun getTvSeries(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = RU_LANGUAGE_CODE
    ): Observable<ApiResponse<Movie>>

    @GET("person/popular")
    fun getPopularPerson(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = RU_LANGUAGE_CODE
    ): Observable<ApiResponse<Person>>


    companion object {
        private const val RU_LANGUAGE_CODE = "ru-RU"
        private const val API_KEY = "PUT THE MOVIE DATABASE API KEY HERE"
    }
}