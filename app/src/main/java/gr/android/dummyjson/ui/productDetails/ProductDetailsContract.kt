package gr.android.dummyjson.ui.productDetails

import androidx.compose.runtime.Stable

interface ProductDetailsContract {

    sealed interface Event {
        data object OnBack: Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val productDetailsScreenInfo: ProductDetailsScreenInfo,
            val product: Product?,
        ): State {

            data class ProductDetailsScreenInfo(
                val title: String,
                val reviewText: String,
                val toolBarInfo: ToolBarInfo,
            ) {
                data class ToolBarInfo(
                    val toolbarLeftIcon: Int,
                    val toolMiddleIcon: Int,
                    val toolRightIcon: Int?,
                    val toolLeftIconVisibility: Boolean,
                    val toolMiddleIconVisibility: Boolean,
                    val toolRightIconVisibility: Boolean
                )
            }

            data class Product(
                val category: String,
                val description: String,
                val id: Int,
                val images: List<String?>,
                val price: String,
                val title: String,
                val reviews: List<Review>,
            ) {
                data class Review(
                    val ratingText: String?,
                    val rating: Int?,
                    val comment: String?,
                    val reviewerName: String?,
                    val date: String?
                )
            }
        }
    }
}