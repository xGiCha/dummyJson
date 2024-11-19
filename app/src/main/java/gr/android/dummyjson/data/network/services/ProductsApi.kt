package gr.android.dummyjson.data.network.services

import gr.android.dummyjson.data.network.models.products.ProductDTO
import gr.android.dummyjson.data.network.models.products.ProductUpdateRequestDTO
import gr.android.dummyjson.data.network.models.products.ProductsDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): List<ProductsDTO>?

    @GET("products/categories")
    suspend fun getProductCategories(): List<String>?

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body productUpdateRequestDTO: ProductUpdateRequestDTO
    ): ProductDTO?
}