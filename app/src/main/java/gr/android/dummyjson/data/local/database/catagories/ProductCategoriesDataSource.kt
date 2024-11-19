package gr.android.dummyjson.data.local.database.catagories

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductCategoriesDataSource @Inject constructor(
    private val productCategoriesDao: ProductCategoriesDao
) {

    suspend fun insertProductCategories(productCategoriesEntity: ProductCategoriesEntity) {
        productCategoriesDao.insertProductCategories(productCategoriesEntity)
    }

    fun getAllProductCategories(): Flow<List<ProductCategoriesEntity>> {
        return productCategoriesDao.getAllProductCategories()
    }

    suspend fun clearTable() {
        productCategoriesDao.clearTable()
    }
}