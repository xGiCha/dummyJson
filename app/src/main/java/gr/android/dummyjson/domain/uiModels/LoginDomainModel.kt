package gr.android.dummyjson.domain.uiModels

data class LoginDomainModel(
    val id: Int,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: String? = null,
    val image: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
