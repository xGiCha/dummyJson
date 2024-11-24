package gr.android.dummyjson.utils

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(call: suspend () -> T): Outcome<T?> {
    return try {
        val response = call()
        Outcome.Success(response)
    } catch (e: IOException) {
        Outcome.Error("Network error: Could not reach the server. Check your internet connection.")
    } catch (e: HttpException) {
        Outcome.Error("HTTP error: ${e.message()} (Code: ${e.code()})")
    } catch (e: Exception) {
        Outcome.Error("Unknown error: ${e.localizedMessage}")
    }
}