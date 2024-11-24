package gr.android.dummyjson.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import gr.android.dummyjson.data.local.database.DummyJsonDatabase
import gr.android.dummyjson.data.local.database.products.ProductEntity
import gr.android.dummyjson.data.local.database.products.ProductsDataSource
import gr.android.dummyjson.data.network.models.products.toEntity
import gr.android.dummyjson.data.network.services.ProductsApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductsPagingDatasource(
    private val productsApi: ProductsApi,
    private val productsDataSource: DummyJsonDatabase,
): RemoteMediator<Int, ProductEntity>() {

//    override fun getRefreshKey(state: PagingState<Int, ProductDTO>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        return anchorPosition / NETWORK_PAGE_SIZE + 1
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDTO> {
//        val pageIndex = params.key ?: STARTING_KEY
//        val skip = (pageIndex - 1) * NETWORK_PAGE_SIZE
//
//        return try {
//            val productsResponse = api.getProducts(limit = NETWORK_PAGE_SIZE, skip = skip)
//            val productItems = productsResponse?.products?.map { it } ?: listOf()
//
//            LoadResult.Page(
//                data = productItems,
//                prevKey = if (pageIndex == STARTING_KEY) null else pageIndex - 1,
//                nextKey = if (productItems.isEmpty()) null else pageIndex + 1
//            )
//        } catch (e: IOException) {
//            // Network error (e.g., no internet connection)
//            LoadResult.Error(IOException("Network Error: Unable to reach server", e))
//        } catch (e: HttpException) {
//            // HTTP error (e.g., 404, 500, etc.)
//            LoadResult.Error(e)
//        } catch (e: Exception) {
//            // Generic error (e.g., unexpected exception)
//            LoadResult.Error(Exception("Unexpected error: ${e.localizedMessage}", e))
//        }
//    }

    companion object {
        private const val STARTING_KEY = 1
        const val NETWORK_PAGE_SIZE = 20
    }

    override suspend fun initialize(): InitializeAction {
        // Decide if we need a full refresh at launch (you can tweak this logic)
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ProductEntity>): MediatorResult {
        val pageIndex = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> {
                // Prepend: Get the previous page index
                return MediatorResult.Success(endOfPaginationReached = true)
//                println("aaaaaaa1 ${(lastItem.id / state.config.pageSize) + 1} ")
//                (lastItem.id / state.config.pageSize) - 1
            }
            LoadType.APPEND -> {
                // Append: Get the next page index
//                (lastItem.id / state.config.pageSize) + 1
                val lastItem = state.lastItemOrNull()

                if(lastItem == null) {
                    println("aaaaaaa4")
                    return MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    println("aaaaaaa2 ${(lastItem.id / state.config.pageSize) + 1} ")
                    (lastItem.id / state.config.pageSize) + 1
                }
            }
        }

        return try {
            println("aaaaaaa3 ${state.config.pageSize} --- ${(pageIndex - 1) * state.config.pageSize}")
            val response = productsApi.getProducts(limit = state.config.pageSize, skip = (pageIndex - 1) * state.config.pageSize)
            val productEntities = response?.products?.map { it.toEntity() } // Convert DTO to Entity

            productsDataSource.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    productsDataSource.dao.clearTable() // Clear old data on refresh
                }
                productEntities?.let {
                    productsDataSource.dao.insertProducts(productEntities) // Insert new data

                }
            }
            MediatorResult.Success(endOfPaginationReached = productEntities?.isEmpty() == true)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }


    }


}
