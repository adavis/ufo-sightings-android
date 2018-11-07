package info.adavis.ufosightings

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class GraphqlDateTimeAdapter : CustomTypeAdapter<Date> {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun decode(value: CustomTypeValue<*>): Date {
        try {
            return dateFormat.parse(value.value as String)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    override fun encode(value: Date): CustomTypeValue<String> {
        return CustomTypeValue.GraphQLString(dateFormat.format(value))
    }
}