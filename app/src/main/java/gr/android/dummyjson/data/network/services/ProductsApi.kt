package gr.android.dummyjson.data.network.services

import gr.android.dummyjson.data.network.models.products.ProductDTO
import gr.android.dummyjson.data.network.models.products.ProductUpdateRequestDTO
import gr.android.dummyjson.data.network.models.products.ProductsDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): ProductsDTO?

    @GET("products/{id}")
    suspend fun getProductDetails(
        @Path("id") productId: Int
    ): ProductDTO?

}