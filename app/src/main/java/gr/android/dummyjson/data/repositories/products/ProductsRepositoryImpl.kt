package gr.android.dummyjson.data.repositories.products

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import gr.android.dummyjson.data.local.database.products.ProductsDataSource
import gr.android.dummyjson.data.network.ProductsPagingDatasource
import gr.android.dummyjson.data.network.models.products.ProductDTO
import gr.android.dummyjson.data.network.models.products.toDomain
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.domain.repository.ProductsRepository
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import retrofit2.HttpException
import java.io.IOException

class ProductsRepositoryImpl(
    private val productsApi: ProductsApi,
    private val productsDataSource: ProductsDataSource,
    private val coroutineScope: CoroutineScope,
): ProductsRepository {

    override val products: Flow<Outcome<PagingData<ProductDomainModel>>> = Pager(
        config = PagingConfig(
            pageSize = ProductsPagingDatasource.NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = ProductsPagingDatasource.NETWORK_PAGE_SIZE
        ),
        pagingSourceFactory = { ProductsPagingDatasource(productsApi) }
    ).flow
        .onStart {
            emit(PagingData.from(listOf()))
        }.catch { e ->
            val errorMessage = when (e) {
                is IOException -> "Network Error: Unable to reach server"
                is HttpException -> "HTTP Error: Status Code ${e.code()}"
                else -> "Unexpected error: ${e.localizedMessage}"
            }
            emit(PagingData.from(listOf())) // Return an empty list with the error message
        }.map { pagingData ->
        // Map the PagingData to the domain model if needed
            Outcome.Success(pagingData.map { productDTO ->
            // Here, we would convert the DTO to the domain model (if necessary)
           productDTO.toDomain()
        })
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}