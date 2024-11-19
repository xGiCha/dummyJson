package gr.android.dummyjson.data.local.database.products

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import gr.android.dummyjson.domain.uiModels.DimensionsDomainModel
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.domain.uiModels.ReviewDomainModel
import gr.android.dummyjson.utils.Constants.PRODUCTS_TABLE

@Entity(tableName = PRODUCTS_TABLE)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,
    val price: Double? = null,
    val discountPercentage: Double? = null,
    val rating: Double? = null,
    val stock: Int? = null,
    @TypeConverters(TagsConverter::class) val tags: List<String>? = null,
    val brand: String? = null,
    @TypeConverters(DimensionsConverter::class) val dimensions: DimensionsEntity? = null,
    val reviews: List<ReviewEntity>? = null,
    val returnPolicy: String? = null,
    val minimumOrderQuantity: Int? = null,
    val thumbnail: String? = null,
    @TypeConverters(ImagesConverter::class) val images: List<String>? = null
) {
    data class DimensionsEntity(
        val width: Double? = null,
        val height: Double? = null,
        val depth: Double? = null
    )

    data class ReviewEntity(
        val rating: Int? = null,
        val comment: String? = null,
        val date: String? = null,
        val reviewerName: String? = null
    )
}

fun ProductEntity.toDomain(): ProductDomainModel {
    return ProductDomainModel(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        dimensions = dimensions?.toDomain(),
        reviews = reviews?.map { it.toDomain() },
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
    )
}

fun ProductEntity.DimensionsEntity.toDomain(): DimensionsDomainModel {
    return DimensionsDomainModel(
        width = width,
        height = height,
        depth = depth
    )
}

fun ProductEntity.ReviewEntity.toDomain(): ReviewDomainModel {
    return ReviewDomainModel(
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName
    )
}
