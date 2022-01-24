package com.yks.movi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.yks.movi.model.MovieResult
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class PopularMovieRepository @Inject constructor(private val popularMoviePagingSource: PopularMoviePagingSource) {
    fun getPopularMovieResult(): Flowable<PagingData<MovieResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {popularMoviePagingSource}
        ).flowable
    }

}