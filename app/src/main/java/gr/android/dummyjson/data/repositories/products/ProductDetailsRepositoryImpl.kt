package gr.android.dummyjson.data.repositories.products

import gr.android.dummyjson.data.network.models.products.toDomain
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.domain.repository.ProductDetailsRepository
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import retrofit2.HttpException
import java.io.IOException

class ProductDetailsRepositoryImpl(
    private val productsApi: ProductsApi,
): ProductDetailsRepository {

    override suspend fun productDetails(productId: Int): Outcome<ProductDomainModel?> {
        return try {
            val productDTO = productsApi.getProductDetails(productId)

            val productDomainModel = productDTO?.toDomain()
            Outcome.Success(productDomainModel)
        } catch (e: IOException) {
            Outcome.Error("Network error: Could not reach the server. Check your internet connection.")
        } catch (e: HttpException) {
            Outcome.Error("HTTP error: ${e.message()} (Code: ${e.code()})")
        } catch (e: Exception) {
            Outcome.Error("Unknown error: ${e.localizedMessage}")
        }
    }

}