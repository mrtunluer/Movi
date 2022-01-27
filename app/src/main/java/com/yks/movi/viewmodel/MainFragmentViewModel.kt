package com.yks.movi.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.yks.movi.model.MovieResult
import com.yks.movi.repository.NowPlayingMovieRepository
import com.yks.movi.repository.PopularMovieRepository
import com.yks.movi.repository.UpcomingMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel
@Inject constructor(
    private val popularMovieRepository: PopularMovieRepository,
    private val upcomingMovieRepository: UpcomingMovieRepository,
    private val nowPlayingMovieRepository: NowPlayingMovieRepository):
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val popularMovieResults = MutableLiveData<PagingData<MovieResult>>()
    val upcomingMovieResults = MutableLiveData<PagingData<MovieResult>>()
    val nowPlayingMovieResults = MutableLiveData<PagingData<MovieResult>>()


    init {
        getPopularMovieData()
        getUpcomingMovieData()
        getNowPlayingMovieData()
    }

    fun getPopularMovieData() {
        compositeDisposable.add(
            popularMovieRepository.getPopularMovieResult()
                .cachedIn(viewModelScope)
                .subscribe {movieResult ->
                    movieResult?.let {
                        popularMovieResults.value = it
                    }
                }
        )
    }

    fun getUpcomingMovieData(){
        compositeDisposable.add(
            upcomingMovieRepository.getUpcomingMovieResult()
                .cachedIn(viewModelScope)
                .subscribe {movieResult ->
                    movieResult?.let {
                        upcomingMovieResults.value = it
                    }
                }
        )
    }

    fun getNowPlayingMovieData(){
        compositeDisposable.add(
            nowPlayingMovieRepository.getNowPlayingMovieResult()
                .cachedIn(viewModelScope)
                .subscribe { movieResult ->
                    movieResult?.let {
                        nowPlayingMovieResults.value = it
                    }
                }
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}


