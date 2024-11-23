package gr.android.dummyjson.data.repositories.products

import gr.android.dummyjson.data.local.database.catagories.ProductCategoriesDataSource
import gr.android.dummyjson.data.local.database.catagories.toDomain
import gr.android.dummyjson.data.local.database.catagories.toEntity
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.domain.repository.ProductCategoriesRepository
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductCategoriesRepositoryImpl(
    private val productsApi: ProductsApi,
    private val productCategoriesDao: ProductCategoriesDataSource,
    private val coroutineScope: CoroutineScope,
): ProductCategoriesRepository {
    private val errors: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _productsCategories =
        productCategoriesDao.getAllProductCategories().onStart {
            getCategoriesFromServer()
        }

    override val productCategories: Flow<Outcome<List<String>?>>
        get() = combine(_productsCategories, errors) { result, e ->
            if (result.isEmpty()) {
                Outcome.Error(e.orEmpty())
            } else {
                Outcome.Success(listOf("All") + result.map { it.toDomain() })
            }
        }

    private fun getCategoriesFromServer() {
        coroutineScope.launch {
            try {
                val data = productsApi.getProductCategories()
                data?.map {
                    productCategoriesDao.insertProductCategories(it.toEntity())
                }
            } catch (e: IOException) {
                errors.emit( "Could not reach the server, please check your internet connection!")
            } catch (e: HttpException) {
                errors.emit("Oops, something went wrong!")
            }

        }
    }
}