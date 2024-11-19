package gr.android.dummyjson.data.network.services

import gr.android.dummyjson.data.network.models.login.LoginDTO
import gr.android.dummyjson.data.network.models.login.LoginRequest
import gr.android.dummyjson.data.network.models.login.RefreshTokenDTO
import gr.android.dummyjson.data.network.models.login.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginDTO

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): RefreshTokenDTO
}