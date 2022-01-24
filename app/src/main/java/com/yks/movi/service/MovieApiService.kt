package com.yks.movi.service

import com.yks.movi.model.ActorProfile
import com.yks.movi.model.MovieDetails
import com.yks.movi.model.Movie
import com.yks.movi.utils.Credentials
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET(Credentials.POPULAR_MOVIE_URL)
    fun getPopularMovie(
        @Query("language")language: String,
        @Query("region")region: String,
        @Query("page")page: Int): Single<Movie>

    @GET(Credentials.UPCOMING_MOVIE_URL)
    fun getUpcomingMovie(
        @Query("language")language: String,
        @Query("region")region: String,
        @Query("page")page: Int): Single<Movie>

    @GET(Credentials.NOW_PLAYING_MOVIE_URL)
    fun getNowPlayingMovie(
        @Query("language")language: String,
        @Query("region")region: String,
        @Query("page")page: Int): Single<Movie>

    @GET(Credentials.SEARCH_URL)
    fun getSearchResult(
        @Query("language")language: String,
        @Query("query")query: String,
        @Query("page")page: Int,
        @Query("include_adult")include_adult: Boolean,
        @Query("region")region: String): Single<Movie>

    @GET(Credentials.ACTOR_MOVIE_CREDITS)
    fun getActor(
        @Path("person_id")person_id: Long,
        @Query("language")language: String,
        @Query("append_to_response")append_to_response: String): Single<ActorProfile>

    @GET(Credentials.MOVIE_DETAIL_URL)
    fun getMovieDetails(
        @Path("movie_id")movie_id: Long,
        @Query("language")language: String,
        @Query("append_to_response")append_to_response: String): Single<MovieDetails>

}