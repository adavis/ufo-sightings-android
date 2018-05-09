package info.adavis.ufosightings.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import info.adavis.ufosightings.SightingsQuery
import info.adavis.ufosightings.util.SingleLiveEvent
import info.adavis.ufosightings.util.doEnqueue

class MainViewModel : ViewModel() {

    private var sightingsState: MutableLiveData<SightingsState>? = null
    private var errorMessage: SingleLiveEvent<String>? = null

    fun getSightings(): LiveData<SightingsState> {
        if (sightingsState == null) {
            sightingsState = MutableLiveData()
            loadSightings()
        }

        return sightingsState as MutableLiveData<SightingsState>
    }

    fun getErrorMessage(): SingleLiveEvent<String> {
        if (errorMessage == null) {
            errorMessage = SingleLiveEvent()
        }

        return errorMessage as SingleLiveEvent<String>
    }

    private fun loadSightings() {
        doEnqueue(
                query = SightingsQuery.builder()
                        .size(30)
                        .build(),

                onResponse = {
                    it.data()?.let {
                        sightingsState?.postValue(SightingsState(it.sightings()))
                    }
                },

                onFailure = {
                    errorMessage?.postValue(it.message)
                }
        )
    }

    override fun onCleared() {
        sightingsState = null
        errorMessage = null

        super.onCleared()
    }
}