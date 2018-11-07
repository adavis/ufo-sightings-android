package info.adavis.ufosightings.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.adavis.ufosightings.SightingsQuery
import info.adavis.ufosightings.util.SingleLiveEvent
import info.adavis.ufosightings.util.enqueueQuery

class MainViewModel : ViewModel() {

    private var sightingsState: MutableLiveData<SightingsState>? = null
    private var errorMessage: SingleLiveEvent<String>? = null

    private val _navigateToAddSighting = SingleLiveEvent<Any>()

    val navigateToAddSighting : LiveData<Any>
        get() = _navigateToAddSighting

    fun getSightings(): LiveData<SightingsState> {
        if (sightingsState == null) {
            sightingsState = MutableLiveData()
            loadSightings()
        }

        return sightingsState as MutableLiveData<SightingsState>
    }

    fun getErrorMessage(): SingleLiveEvent<String> {
        if (errorMessage == null) errorMessage = SingleLiveEvent()

        return errorMessage as SingleLiveEvent<String>
    }

    private fun loadSightings() {
        enqueueQuery(
                query = SightingsQuery.builder()
                        .size(30)
                        .build(),

                onResponse = {
                    it.data()?.let { data ->
                        sightingsState?.postValue(SightingsState(data.sightings()))
                    }
                },

                onFailure = {
                    errorMessage?.postValue(it.message)
                }
        )
    }

    fun handleAddSightingClick() {
        _navigateToAddSighting.call()
    }

    override fun onCleared() {
        sightingsState = null
        errorMessage = null

        super.onCleared()
    }
}