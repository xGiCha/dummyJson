package gr.android.dummyjson.data.network.services

import gr.android.dummyjson.data.network.models.login.LoginDTO
import gr.android.dummyjson.data.network.models.login.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginDTO
}