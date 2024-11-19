package gr.android.dummyjson.data.local.database.catagories

import androidx.room.Entity
import androidx.room.PrimaryKey
import gr.android.dummyjson.utils.Constants.PRODUCT_CATEGORY_TABLE

@Entity(tableName = PRODUCT_CATEGORY_TABLE)
data class ProductCategoriesEntity(
    @PrimaryKey(autoGenerate = false)
    val category: String,
)

fun ProductCategoriesEntity.toDomain(): String {
    return this.category
}

fun String.toEntity(): ProductCategoriesEntity {
    return ProductCategoriesEntity(category = this)
}
