package gr.android.dummyjson.domain.repository

import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {
    suspend fun productDetails(productId: Int): Outcome<ProductDomainModel?>
}