package gr.android.dummyjson.domain.repository

import androidx.paging.PagingData
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    val products: Flow<PagingData<ProductDomainModel>>
}