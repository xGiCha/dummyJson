package gr.android.dummyjson.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gr.android.dummyjson.data.network.models.products.ProductDTO
import gr.android.dummyjson.data.network.services.ProductsApi
import retrofit2.HttpException
import java.io.IOException

class ProductsPagingDatasource(
    private val api: ProductsApi,
): PagingSource<Int,ProductDTO>() {

    override fun getRefreshKey(state: PagingState<Int, ProductDTO>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return anchorPosition / NETWORK_PAGE_SIZE + 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDTO> {
        val pageIndex = params.key ?: STARTING_KEY
        val skip = (pageIndex - 1) * NETWORK_PAGE_SIZE

        return try {
            val productsResponse = api.getProducts(limit = NETWORK_PAGE_SIZE, skip = skip)
            val productItems = productsResponse?.products?.map { it } ?: listOf()

            LoadResult.Page(
                data = productItems,
                prevKey = if (pageIndex == STARTING_KEY) null else pageIndex - 1,
                nextKey = if (productItems.isEmpty()) null else pageIndex + 1
            )
        } catch (e: IOException) {
            // Network error (e.g., no internet connection)
            LoadResult.Error(IOException("Network Error: Unable to reach server", e))
        } catch (e: HttpException) {
            // HTTP error (e.g., 404, 500, etc.)
            LoadResult.Error(e)
        } catch (e: Exception) {
            // Generic error (e.g., unexpected exception)
            LoadResult.Error(Exception("Unexpected error: ${e.localizedMessage}", e))
        }
    }

    companion object {
        private const val STARTING_KEY = 1
        const val NETWORK_PAGE_SIZE = 20
    }
}
