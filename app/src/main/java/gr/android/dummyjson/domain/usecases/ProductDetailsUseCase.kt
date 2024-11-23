package gr.android.dummyjson.domain.usecases

import gr.android.dummyjson.domain.repository.ProductDetailsRepository
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductDetailsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository,
) {
    fun invoke(productId: Int): Flow<Outcome<ProductDomainModel?>> {
        return flow {
            emit(
                productDetailsRepository.productDetails(productId)
            )
        }
    }
}