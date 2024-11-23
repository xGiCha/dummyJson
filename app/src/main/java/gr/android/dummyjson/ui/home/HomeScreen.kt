package gr.android.dummyjson.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import gr.android.dummyjson.R
import gr.android.dummyjson.ui.composables.ProductItemModal
import gr.android.dummyjson.ui.composables.TopBarModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    when(val state = viewModel.uiState.collectAsStateWithLifecycle().value){
        is HomeContract.State.Data -> {
            HomeScreenContent(
                products = state.products,
                homeScreenInfo = state.homeScreenInfo,
            )
        }
        is HomeContract.State.Error -> {}
        else -> {}
    }
}

@Composable
private fun HomeScreenContent(
    products: Flow<PagingData<HomeContract.State.Data.Product>>,
    homeScreenInfo: HomeContract.State.Data.HomeScreenInfo,
) {

    val lazyPagingItems = products.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()

    ) {

        TopBarModal(
            leftIconVisibility = homeScreenInfo.toolbarInfo.toolLeftIconVisibility,
            onRightClick = {
//                onProfileClick()
            }
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            ProductItemModal(
                product = lazyPagingItems[index],
                onProductClick = { /* Handle product click */ }
            )
        }
    }

}
}

@Preview
@Composable
private fun HomeScreenContentPreview() {
    val product = HomeContract.State.Data.Product(
        "All",
        id = 5,
        title = "Woman Printed Kurta",
        image = "",
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = "1500"
    )
    HomeScreenContent(
        products = flowOf(PagingData.from(listOf(product, product, product, product))),
        homeScreenInfo = HomeContract.State.Data.HomeScreenInfo(
            allFeaturedTitle = "All Featured",
            toolbarInfo = HomeContract.State.Data.HomeScreenInfo.ToolBarInfo(
                toolbarLeftIcon = R.drawable.ic_left_arrow,
                toolMiddleIcon = R.drawable.ic_toolbar,
                toolRightIcon = R.drawable.ic_profile,
                toolLeftIconVisibility = false,
                toolMiddleIconVisibility = true,
                toolRightIconVisibility = true
            )
        )
    )
}