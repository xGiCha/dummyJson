package gr.android.dummyjson.domain.repository

import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface ProductCategoriesRepository {
    val productCategories: Flow<Outcome<List<String>?>>
}