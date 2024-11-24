package gr.android.dummyjson.data.network.services.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenService: TokenService // Inject TokenService here
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Check if we've already retried this request to avoid infinite retries
        if (responseCount(response) >= 1) {
            return null // Stop retrying if we've already tried once
        }

        return runBlocking {
            try {
                // Call the refresh token API
                val newAccessToken = tokenService.refreshAccessToken()

                if (newAccessToken != null) {
                    // Retry the original request with the new access token
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                }
            } catch (e: Exception) {
                // Handle any error here
            }
            null // Return null if refresh fails
        }
    }

    // Helper function to count retries to prevent infinite loops
    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

}

