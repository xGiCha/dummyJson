package gr.android.dummyjson.ui.home

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface HomeContract {

    sealed interface Event {
        data class NavigateToDetailsScreen(val productId: Int) : Event
        data object NavigateToLoginScreen : Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val homeScreenInfo: HomeScreenInfo,
            val products: Flow<PagingData<Product>>,
            val showLogout: Boolean,
        ): State {
            data class HomeScreenInfo(
                val allFeaturedTitle: String,
                val toolbarInfo: ToolBarInfo,
            ) {
                data class ToolBarInfo(
                    val toolbarLeftIcon: Int,
                    val toolMiddleIcon: Int,
                    val toolRightIcon: Int,
                    val toolLeftIconVisibility: Boolean,
                    val toolMiddleIconVisibility: Boolean,
                    val toolRightIconVisibility: Boolean
                )
            }

            data class Product(
                val category: String,
                val description: String,
                val id: Int,
                val image: String,
                val price: String,
                val title: String,
                val brand: String,
            )
        }
    }
}