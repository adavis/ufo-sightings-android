package info.adavis.ufosightings.util

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import info.adavis.ufosightings.Injection.apolloClient

inline fun <D : Operation.Data, T, V : Operation.Variables> doEnqueue(
        query: Query<D, T, V>,
        crossinline onResponse: (response: Response<T>) -> Unit,
        crossinline onFailure: (e: ApolloException) -> Unit
) {
    apolloClient.query(query).enqueue(object : ApolloCall.Callback<T>() {
        override fun onResponse(response: Response<T>) {
            onResponse(response)
        }

        override fun onFailure(e: ApolloException) {
            onFailure(e)
        }

    })
}