package info.adavis.ufosightings.home

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import info.adavis.ufosightings.Injection.apolloClient
import info.adavis.ufosightings.SightingsQuery

class MainPresenter constructor(private var view: MainView?) {

    fun detachView() {
        view = null
    }

    fun getTopSightings() {
        doEnqueue(
                query = SightingsQuery.builder()
                        .size(30)
                        .build(),

                onResponse = {
                    it.data()?.let { view?.displaySightings(it.sightings()) }
                },

                onFailure = {
                    view?.displayError(it.message)
                }
        )
    }

}

fun doEnqueue(
        query: SightingsQuery,
        onResponse: (response: Response<SightingsQuery.Data>) -> Unit,
        onFailure: (e: ApolloException) -> Unit
) {
    apolloClient.query(query).enqueue(object : ApolloCall.Callback<SightingsQuery.Data>() {
        override fun onFailure(e: ApolloException) {
            onFailure(e)
        }

        override fun onResponse(response: Response<SightingsQuery.Data>) {
            onResponse(response)
        }

    })
}