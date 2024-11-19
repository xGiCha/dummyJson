package gr.android.dummyjson.data.network.models.login

import com.google.gson.annotations.SerializedName
import gr.android.dummyjson.domain.uiModels.LoginDomainModel

data class LoginDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null
)

fun LoginDTO.toDomain(): LoginDomainModel {
    return LoginDomainModel(
        id = this.id,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        gender = this.gender,
        image = this.image,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}