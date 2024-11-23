package gr.android.dummyjson.domain.usecases

import gr.android.dummyjson.domain.repository.ProductCategoriesRepository
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val productCategoriesRepository: ProductCategoriesRepository
) {

    operator fun invoke(): Flow<Outcome<List<String>?>> {
        return productCategoriesRepository.productCategories
    }
}