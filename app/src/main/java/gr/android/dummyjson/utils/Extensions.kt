package gr.android.dummyjson.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.formatDate(): String? {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")

        val desiredFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        val date = originalFormat.parse(this)
        date?.let { desiredFormat.format(it) }
    } catch (e: Exception) {
        null
    }
}