package gr.android.dummyjson.data.local.database.products

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsDataSource @Inject constructor(
    private val productsDao: ProductsDao
) {

    suspend fun insertProduct(productEntity: ProductEntity) {
        productsDao.insertProduct(productEntity)
    }

    fun getAllProducts(): PagingSource<Int, ProductEntity> {
        return productsDao.getAllProducts()
    }

    suspend fun clearTable() {
        productsDao.clearTable()
    }

    suspend fun insertProducts(products: List<ProductEntity>) {
        productsDao.insertProducts(products)
    }

}