package com.yks.movi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.yks.movi.model.MovieResult
import com.yks.movi.repository.SearchResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val searchResultRepository: SearchResultRepository):
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val searchResults = MutableLiveData<PagingData<MovieResult>>()

    init {
        getSearchResult()
    }

    fun getSearchResult(){
        compositeDisposable.add(
            searchResultRepository.getSearchResult()
                .cachedIn(viewModelScope)
                .subscribe{result ->
                    result?.let {
                        searchResults.value = it
                    }
                }
        )
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}