package gr.android.dummyjson.data.network.models.products

import com.google.gson.annotations.SerializedName
import gr.android.dummyjson.data.local.database.products.ProductEntity
import gr.android.dummyjson.domain.uiModels.DimensionsDomainModel
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.domain.uiModels.ReviewDomainModel


data class ProductsDTO(
    val products: List<ProductDTO>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
data class ProductDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discountPercentage") val discountPercentage: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("brand") val brand: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("dimensions") val dimensions: Dimensions,
    @SerializedName("warrantyInformation") val warrantyInformation: String,
    @SerializedName("shippingInformation") val shippingInformation: String,
    @SerializedName("availabilityStatus") val availabilityStatus: String,
    @SerializedName("reviews") val reviews: List<Review>,
    @SerializedName("returnPolicy") val returnPolicy: String,
    @SerializedName("minimumOrderQuantity") val minimumOrderQuantity: Int,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("images") val images: List<String>
) {
    data class Dimensions(
        @SerializedName("width") val width: Double,
        @SerializedName("height") val height: Double,
        @SerializedName("depth") val depth: Double
    )

    data class Review(
        @SerializedName("rating") val rating: Int,
        @SerializedName("comment") val comment: String,
        @SerializedName("date") val date: String,
        @SerializedName("reviewerName") val reviewerName: String,
        @SerializedName("reviewerEmail") val reviewerEmail: String
    )

    data class Meta(
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("updatedAt") val updatedAt: String,
        @SerializedName("barcode") val barcode: String,
        @SerializedName("qrCode") val qrCode: String?
    )
}

fun ProductDTO.toDomain(): ProductDomainModel {
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
        dimensions = dimensions.toDomain(),
        reviews = reviews.map { it.toDomain() },
        minimumOrderQuantity = minimumOrderQuantity,
        images = images
    )
}

fun ProductDTO.Dimensions.toDomain(): DimensionsDomainModel {
    return DimensionsDomainModel(
        width = width,
        height = height,
        depth = depth
    )
}

fun ProductDTO.Review.toDomain(): ReviewDomainModel {
    return ReviewDomainModel(
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName
    )
}

fun ProductDTO.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        dimensions = dimensions.toEntity(),
        reviews = reviews.map { it.toEntity() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images
    )
}

fun ProductDTO.Dimensions.toEntity(): ProductEntity.DimensionsEntity {
    return ProductEntity.DimensionsEntity(
        width = width,
        height = height,
        depth = depth
    )
}

fun ProductDTO.Review.toEntity(): ProductEntity.ReviewEntity {
    return ProductEntity.ReviewEntity(
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName
    )
}
