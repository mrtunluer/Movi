package com.yks.movi.repository

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.yks.movi.model.MovieResult
import com.yks.movi.service.MovieApiService
import com.yks.movi.utils.Credentials
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpcomingMoviePagingSource @Inject constructor(
    private val movieApiService: MovieApiService
): RxPagingSource<Int, MovieResult>() {
    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, MovieResult>> {
        val page = params.key ?: 1
        return movieApiService.getUpcomingMovie(Credentials.LANGUAGE, Credentials.REGION,page)
            .subscribeOn(Schedulers.io())
            .map { upcomingMovie ->
                upcomingMovie?.let {
                    LoadResult.Page(
                        data = it.movieResults!!,
                        prevKey = if(page == 1) null else page-1,
                        nextKey = if (page == it.totalPages) null else page+1
                    ) as LoadResult<Int,MovieResult>
                }
            }.onErrorReturn {
                LoadResult.Error(it)
            }
    }
}