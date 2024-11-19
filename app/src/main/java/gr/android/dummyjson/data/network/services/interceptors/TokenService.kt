package gr.android.dummyjson.data.network.services.interceptors
import gr.android.dummyjson.data.network.models.login.RefreshTokenRequest
import gr.android.dummyjson.data.local.SessionPreferences
import gr.android.dummyjson.data.network.datasources.LoginDatasource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenService @Inject constructor(
    private val loginDatasource: LoginDatasource,
    private val sessionPreferences: SessionPreferences
) {

    // Function to refresh the token
    suspend fun refreshAccessToken(): String? {
        val refreshToken = sessionPreferences.getRefreshToken.firstOrNull()

        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        try {
            val response = loginDatasource.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.refreshToken.isNotEmpty()) {
                // Save new tokens in session preferences
                sessionPreferences.saveAccessToken(response.accessToken)
                sessionPreferences.saveRefreshToken(response.refreshToken)
                return response.accessToken // Return the new access token
            }
        } catch (e: Exception) {
            // Handle any exception, like network error or invalid refresh token
        }
        return null
    }
}
