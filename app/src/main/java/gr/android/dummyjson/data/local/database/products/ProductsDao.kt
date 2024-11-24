package gr.android.dummyjson.data.local.database.products

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("DELETE FROM products_table")
    suspend fun clearTable()

    @Query("SELECT * FROM products_table")
    fun getAllProducts(): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)
}