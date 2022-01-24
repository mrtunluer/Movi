package com.yks.movi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yks.movi.model.ActorProfile
import com.yks.movi.repository.ActorRepository
import com.yks.movi.status.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ActorFragmentViewModel
@Inject constructor(
    private val actorRepository: ActorRepository,
    private val savedStateHandle: SavedStateHandle):
    ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    val actorProfile: MutableLiveData<DataStatus<ActorProfile>> by lazy {
        MutableLiveData<DataStatus<ActorProfile>>()
    }

    init {
        getActorProfile()
    }

    fun getActorProfile(){
        compositeDisposable.addAll(
            actorRepository.getActor(savedStateHandle.get<Long>("personId")!!)
                .doOnSubscribe{actorProfile.value = DataStatus.Loading()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(//first parameter onSuccess , second parameter onError
                    {details -> actorProfile.value = DataStatus.Success(details)},
                    {error -> actorProfile.value = DataStatus.Error(error.message)}))
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}