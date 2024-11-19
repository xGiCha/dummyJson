package gr.android.dummyjson.data.network.datasources

import gr.android.dummyjson.data.network.models.login.LoginDTO
import gr.android.dummyjson.data.network.models.login.LoginRequest
import gr.android.dummyjson.data.network.models.login.RefreshTokenDTO
import gr.android.dummyjson.data.network.models.login.RefreshTokenRequest
import gr.android.dummyjson.data.network.services.LoginApi

interface LoginDatasource {
    suspend fun loginUser(loginRequest: LoginRequest): LoginDTO
    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenDTO
}
class LoginDatasourceImpl(
    private val api: LoginApi
) : LoginDatasource {

    override suspend fun loginUser(loginRequest: LoginRequest): LoginDTO {
        return api.loginUser(loginRequest)
    }

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenDTO {
        return api.refreshToken(refreshTokenRequest)
    }
}