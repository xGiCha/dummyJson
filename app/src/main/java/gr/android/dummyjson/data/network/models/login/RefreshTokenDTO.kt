package gr.android.dummyjson.data.network.models.login

import com.google.gson.annotations.SerializedName

data class RefreshTokenDTO(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)
