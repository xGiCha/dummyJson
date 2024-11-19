package gr.android.dummyjson.data.network.services.interceptors
import gr.android.dummyjson.data.network.models.login.RefreshTokenRequest
import gr.android.dummyjson.data.local.SessionPreferences
import gr.android.dummyjson.data.network.models.login.RefreshTokenDTO
import gr.android.dummyjson.data.network.services.LoginApi
import gr.android.dummyjson.utils.Constants.BASE_URL
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TokenService @Inject constructor(
    private val sessionPreferences: SessionPreferences
) {

    // Function to refresh the token
    suspend fun refreshAccessToken(): String? {
        val refreshToken = sessionPreferences.getRefreshToken.firstOrNull()

        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        try {
            val response = getNewToken(refreshToken)
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

    private suspend fun getNewToken(refreshToken: String?): RefreshTokenDTO {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(LoginApi::class.java)
        return service.refreshToken(RefreshTokenRequest(refreshToken.orEmpty()))
    }
}
