package gr.android.dummyjson.data.repositories.login

import gr.android.dummyjson.data.local.SessionPreferences
import gr.android.dummyjson.data.network.datasources.LoginDatasource
import gr.android.dummyjson.data.network.models.login.LoginRequest
import gr.android.dummyjson.domain.repository.LoginRepository
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class LoginRepositoryImpl(
    private val datasource: LoginDatasource,
    private val sessionPreferences: SessionPreferences
): LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Outcome<Unit?> {
        return try {
            val result = datasource.loginUser(loginRequest)
            if (result.accessToken?.isNotEmpty() == true ){
                sessionPreferences.saveAccessToken(result.accessToken ?: "")
                sessionPreferences.saveRefreshToken(result.refreshToken ?: "")
                Outcome.Success(Unit)
            } else {
                Outcome.Error(message = "Login failed. Please check your credentials and try again.")
            }
        } catch (e: IOException) {
            Outcome.Error(message = "Unable to connect to the server. Please check your internet connection and try again.")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Outcome.Error(message = "Unauthorized: Invalid credentials or session expired.")
            } else {
                Outcome.Error(message = "Unexpected error occurred. Please try again later.")
            }
        }
    }

    override suspend fun isLoggedIn(): Outcome<Boolean> {
        return if (sessionPreferences.getAccessToken.first().isEmpty()) {
            Outcome.Error("Error")
        } else {
            Outcome.Success(true)
        }
    }
}