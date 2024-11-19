package gr.android.dummyjson.data.network.models.login

import com.google.gson.annotations.SerializedName

data class LoginDTO(
    @SerializedName("token")
    val token: String
)