package com.yks.movi.repository

import com.yks.movi.model.ActorProfile
import com.yks.movi.service.MovieApiService
import com.yks.movi.utils.Credentials
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class ActorRepository @Inject constructor(private val movieApiService: MovieApiService) {
    fun getActor(personId: Long): Single<ActorProfile> {
        return movieApiService.getActor(personId, Credentials.LANGUAGE, Credentials.APPEND_TO_RESPONSE_FOR_ACTOR)
            .subscribeOn(Schedulers.io())
    }
}