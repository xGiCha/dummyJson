package gr.android.dummyjson.domain.repository

import gr.android.dummyjson.data.network.models.login.LoginRequest
import gr.android.dummyjson.data.network.models.login.RefreshTokenDTO
import gr.android.dummyjson.utils.Outcome

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Outcome<Unit?>
    suspend fun isLoggedIn(): Outcome<Boolean>
    suspend fun refreshToken(refreshToken: String): RefreshTokenDTO
}