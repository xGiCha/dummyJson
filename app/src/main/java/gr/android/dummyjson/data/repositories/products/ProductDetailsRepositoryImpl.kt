package gr.android.dummyjson.data.repositories.products

import gr.android.dummyjson.data.network.models.products.toDomain
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.domain.repository.ProductDetailsRepository
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import gr.android.dummyjson.utils.safeApiCall
import retrofit2.HttpException
import java.io.IOException

class ProductDetailsRepositoryImpl(
    private val productsApi: ProductsApi,
): ProductDetailsRepository {

    override suspend fun productDetails(productId: Int): Outcome<ProductDomainModel?> {
        return safeApiCall { productsApi.getProductDetails(productId)?.toDomain() }
    }
}