package gr.android.dummyjson.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import gr.android.dummyjson.R
import gr.android.dummyjson.ui.composables.ProductItemModal
import gr.android.dummyjson.ui.composables.SmallMessageModal
import gr.android.dummyjson.ui.composables.TopBarModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed interface HomeNavigation {
    data class NavigateToDetails(val productId: Int) : HomeNavigation
    data object NavigateToLoginScreen : HomeNavigation
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigate: (HomeNavigation) -> Unit
) {

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeContract.Event.NavigateToDetailsScreen -> {
                    navigate(HomeNavigation.NavigateToDetails(event.productId))
                }

                HomeContract.Event.NavigateToLoginScreen -> {
                    navigate(HomeNavigation.NavigateToLoginScreen)
                }
            }
        }
    }

    when (val state = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is HomeContract.State.Data -> {
            HomeScreenContent(
                products = state.products,
                homeScreenInfo = state.homeScreenInfo,
                showErrorMessage = {
                    viewModel.setErrorMessage(it)
                },
                navigate = {
                    when (it) {
                        is HomeNavigation.NavigateToDetails -> {
                            viewModel.navigateToDetails(it.productId)
                        }

                        HomeNavigation.NavigateToLoginScreen -> {}
                    }
                },
                onProfileClick = {
                    viewModel.showLogout()
                },
                showLogout = state.showLogout,
                onLogoutClick = {
                    viewModel.logout()
                    viewModel.navigateToLogin()
                },
                onHideLogout = {
                    viewModel.hideLogout()
                }
            )
        }

        is HomeContract.State.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SmallMessageModal(
                    errorMessage = state.value,
                    onClick = {
                        viewModel.refresh()
                    },
                )
            }
        }
        else -> {}
    }
}

@Composable
private fun HomeScreenContent(
    products: Flow<PagingData<HomeContract.State.Data.Product>>,
    homeScreenInfo: HomeContract.State.Data.HomeScreenInfo,
    showErrorMessage: (String) -> Unit,
    navigate: (HomeNavigation) -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    showLogout: Boolean,
    onHideLogout: () -> Unit,
) {

    val lazyPagingItems = products.collectAsLazyPagingItems()

    LaunchedEffect(lazyPagingItems.loadState) {
        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            showErrorMessage((lazyPagingItems.loadState.refresh as LoadState.Error).error.message.orEmpty())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {

            TopBarModal(
                leftIconVisibility = homeScreenInfo.toolbarInfo.toolLeftIconVisibility,
                onRightClick = {
                onProfileClick()
                }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items(lazyPagingItems.itemCount) { index ->
                    ProductItemModal(
                        product = lazyPagingItems[index],
                        onProductClick = { navigate(HomeNavigation.NavigateToDetails(it.id)) }
                    )
                }
                item(
                    span = { GridItemSpan(2) }
                ) {
                    when (lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is LoadState.Error -> {
                            Button(
                                onClick = { lazyPagingItems.retry() },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = stringResource(R.string.try_again))
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        if (showLogout) {
            SmallMessageModal(
                isCloseVisible = true,
                errorMessage = stringResource(R.string.profile_coming_soon),
                buttonText = stringResource(R.string.logout),
                onClick = {
                    onHideLogout()
                    onLogoutClick()
                },
                onDismiss = {
                    onHideLogout()
                }
            )
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
        price = "1500",
        brand = "Nike"
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
        ),
        showErrorMessage = {},
        navigate = {},
        onLogoutClick = {},
        onProfileClick = {},
        showLogout = false,
        onHideLogout = {}
    )
}