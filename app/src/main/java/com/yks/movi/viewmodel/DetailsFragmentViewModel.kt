package com.yks.movi.viewmodel

import androidx.lifecycle.*
import com.yks.movi.model.MovieDetails
import com.yks.movi.status.DataStatus
import com.yks.movi.repository.MovieDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel
@Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val savedStateHandle: SavedStateHandle):
    ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    val movieDetails = MutableLiveData<DataStatus<MovieDetails>>()

    init {
        getMovieDetails()
    }

    fun getMovieDetails(){
        compositeDisposable.addAll(
            movieDetailsRepository.getMovieDetails(savedStateHandle.get<Long>("movieId")!!)
                .doOnSubscribe{movieDetails.value = DataStatus.Loading()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(//first parameter onSuccess , second parameter onError
                    {details -> movieDetails.value = DataStatus.Success(details)},
                    {error -> movieDetails.value = DataStatus.Error(error.message)}))
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}