package com.yks.movi.repository

import com.yks.movi.model.MovieDetails
import com.yks.movi.service.MovieApiService
import com.yks.movi.utils.Credentials
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(private val movieApiService: MovieApiService) {
    fun getMovieDetails(movieId: Long): Single<MovieDetails>{
        return movieApiService.getMovieDetails(movieId,Credentials.LANGUAGE,Credentials.APPEND_TO_RESPONSE)
            .subscribeOn(Schedulers.io())
    }
}