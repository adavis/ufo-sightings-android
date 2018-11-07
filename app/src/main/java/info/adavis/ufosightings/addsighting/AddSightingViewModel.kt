package info.adavis.ufosightings.addsighting

import androidx.lifecycle.ViewModel
import info.adavis.ufosightings.CreateSightingMutation
import info.adavis.ufosightings.type.CreateUFOSightingInput
import info.adavis.ufosightings.util.SingleLiveEvent
import info.adavis.ufosightings.util.enqueueMutation

class AddSightingViewModel : ViewModel() {

    private var errorMessage: SingleLiveEvent<String>? = null
    private var successMessage: SingleLiveEvent<String>? = null

    fun getErrorMessage(): SingleLiveEvent<String> {
        if (errorMessage == null) errorMessage = SingleLiveEvent()

        return errorMessage as SingleLiveEvent<String>
    }

    fun getSuccessMessage(): SingleLiveEvent<String> {
        if (successMessage == null) successMessage = SingleLiveEvent()

        return successMessage as SingleLiveEvent<String>
    }

    fun handleSaveSighting(
            city: String,
            state: String,
            country: String,
            shape: String,
            comments: String
    ) {
        enqueueMutation(
                mutation = CreateSightingMutation.builder()
                        .newSighting(CreateUFOSightingInput.builder()
                                             .city(city)
                                             .state(state)
                                             .shape(shape)
                                             .country(country)
                                             .comments(comments)
                                             .build())
                        .build(),

                onResponse = {
                    it.data()?.createUFOSighting()?.id()?.let {
                        successMessage?.postValue("UFO Sighting recorded")
                    }
                },

                onFailure = {
                    errorMessage?.postValue(it.message)
                }
        )
    }

    override fun onCleared() {
        errorMessage = null
        successMessage = null

        super.onCleared()
    }
}