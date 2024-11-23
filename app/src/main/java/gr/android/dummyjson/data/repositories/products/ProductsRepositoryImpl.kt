package gr.android.dummyjson.data.repositories.products

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import gr.android.dummyjson.data.local.database.products.ProductsDataSource
import gr.android.dummyjson.data.network.ProductsPagingDatasource
import gr.android.dummyjson.data.network.models.products.toDomain
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.domain.repository.ProductsRepository
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl(
    private val productsApi: ProductsApi,
    private val productsDataSource: ProductsDataSource,
    coroutineScope: CoroutineScope,
) : ProductsRepository {

    override val products: Flow<PagingData<ProductDomainModel>> by lazy {
        Pager(
            config = PagingConfig(
                pageSize = ProductsPagingDatasource.NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = ProductsPagingDatasource.NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { ProductsPagingDatasource(productsApi) }
        ).flow.cachedIn(coroutineScope).map { pagingData ->
            pagingData.map { productDTO ->
                productDTO.toDomain()
            }
        }
    }
}