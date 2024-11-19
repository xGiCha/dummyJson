package gr.android.dummyjson.data.local.database.products

import androidx.room.Entity
import androidx.room.PrimaryKey
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.utils.Constants.PRODUCTS_TABLE

@Entity(tableName = PRODUCTS_TABLE)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null,
    val price: Double? = null,
    val title: String? = null
)

fun ProductEntity.toDomain(): ProductDomainModel {
    return ProductDomainModel(
        category = category,
        description = description,
        id = id,
        image = image,
        price = price,
        title = title
    )
}
