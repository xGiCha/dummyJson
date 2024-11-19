package gr.android.dummyjson.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gr.android.dummyjson.data.local.database.catagories.ProductCategoriesDao
import gr.android.dummyjson.data.local.database.catagories.ProductCategoriesEntity
import gr.android.dummyjson.data.local.database.products.ProductEntity
import gr.android.dummyjson.data.local.database.products.ProductsDao
import gr.android.dummyjson.data.local.database.typeConverts.DimensionsTypeConverter
import gr.android.dummyjson.data.local.database.typeConverts.ImagesTypeConverter
import gr.android.dummyjson.data.local.database.typeConverts.ReviewsTypeConverter

@Database(
    entities = [
        ProductEntity::class,
        ProductCategoriesEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    ReviewsTypeConverter::class,
    ImagesTypeConverter::class,
    DimensionsTypeConverter::class
)
abstract class DummyJsonDatabase : RoomDatabase() {
    abstract val dao: ProductsDao
    abstract val productCategoriesDao: ProductCategoriesDao
}