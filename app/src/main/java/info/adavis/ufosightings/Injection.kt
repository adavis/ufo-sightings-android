package info.adavis.ufosightings

import com.apollographql.apollo.ApolloClient
import info.adavis.ufosightings.type.CustomType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

private const val BASE_URL = "http://localhost:8080/graphql"

object Injection {

    val apolloClient: ApolloClient by lazy {
        ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.LOCALDATE, GraphqlDateTimeAdapter())
                .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val httpLoggingInterceptor: HttpLoggingInterceptor
        get() {
            return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.d(message) })
                    .apply { level = HttpLoggingInterceptor.Level.BODY }
        }

}