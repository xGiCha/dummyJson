package gr.android.dummyjson.domain.uiModels

data class ProductDomainModel(
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,
    val price: Double? = null,
    val discountPercentage: Double? = null,
    val rating: Double? = null,
    val stock: Int? = null,
    val brand: String? = null,
    val dimensions: DimensionsDomainModel? = null,
    val availabilityStatus: String? = null,
    val reviews: List<ReviewDomainModel>? = null,
    val minimumOrderQuantity: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val images: List<String>? = null
)

data class DimensionsDomainModel(
    val width: Double? = null,
    val height: Double? = null,
    val depth: Double? = null
)

data class ReviewDomainModel(
    val rating: Int? = null,
    val comment: String? = null,
    val reviewerName: String? = null,
    val date: String? = null
)
