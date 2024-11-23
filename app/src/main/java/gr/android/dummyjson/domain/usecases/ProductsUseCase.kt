package gr.android.dummyjson.domain.usecases

import androidx.paging.PagingData
import gr.android.dummyjson.domain.repository.ProductsRepository
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    operator fun invoke(): Flow<PagingData<ProductDomainModel>> {
        return productsRepository.products
    }
}