package info.adavis.ufosightings

import com.apollographql.apollo.CustomTypeAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class GraphqlDateTimeAdapter : CustomTypeAdapter<Date> {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun decode(value: String): Date {
        try {
            return dateFormat.parse(value)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    override fun encode(value: Date): String {
        return dateFormat.format(value)
    }
}