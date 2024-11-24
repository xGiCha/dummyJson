package gr.android.dummyjson.ui.productDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.android.dummyjson.R
import gr.android.dummyjson.ui.composables.CarouseItemModal
import gr.android.dummyjson.ui.composables.CarouselModal
import gr.android.dummyjson.ui.composables.ReviewList
import gr.android.dummyjson.ui.composables.SmallMessageModal
import gr.android.dummyjson.ui.composables.TopBarModal
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.Product.Review
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.ProductDetailsScreenInfo
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo

sealed interface ProductDetailsScreenNavigation {
    data object OnBack : ProductDetailsScreenNavigation
}

@Composable
fun ProductDetailsScreen(
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel(),
    productId: Int?,
    navigate: (ProductDetailsScreenNavigation) -> Unit
) {
    productDetailsViewModel.getProduct(productId = productId ?: -1)

    LaunchedEffect(productDetailsViewModel.events) {
        productDetailsViewModel.events.collect {
            when (it) {
                ProductDetailsContract.Event.OnBack -> {
                    navigate(ProductDetailsScreenNavigation.OnBack)
                }
            }
        }
    }
    when (val state = productDetailsViewModel.uiState.collectAsStateWithLifecycle().value) {
        is ProductDetailsContract.State.Data -> {
            ProductDetailsScreenContent(
                product = state.product,
                productDetailsScreenInfo = state.productDetailsScreenInfo,
                navigate = {
                    when (it) {
                        ProductDetailsScreenNavigation.OnBack -> {
                            productDetailsViewModel.onBack()
                        }
                    }
                }
            )
        }

        is ProductDetailsContract.State.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SmallMessageModal(
                    errorMessage = state.value,
                    onClick = {
                        productDetailsViewModel.getProduct(productId = productId ?: -1)
                    },
                )
            }
        }
        else -> {}
    }
}

@Composable
fun ProductDetailsScreenContent(
    product: ProductDetailsContract.State.Data.Product?,
    productDetailsScreenInfo: ProductDetailsScreenInfo,
    navigate: (ProductDetailsScreenNavigation) -> Unit,
) {
    val isExpanded = remember { mutableStateOf(false) }
    val showReadMore = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBarModal(
            middleIconVisibility = productDetailsScreenInfo.toolBarInfo.toolMiddleIconVisibility,
            rightIcon = null,
            rightIconRoundedCorners = RoundedCornerShape(0.dp),
            onBackClick = {
                navigate(ProductDetailsScreenNavigation.OnBack)
            },
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            CarouselModal(
                item = { pagerState, index ->
                    CarouseItemModal(
                        image = product?.images?.get(index).orEmpty(),
                    )
                },
                size = product?.images?.size ?: 0,
                paddingValues = PaddingValues(horizontal = 16.dp), // Horizontal padding at the start
                spacedBy = 16.dp
            )

            Text(
                modifier = Modifier.padding(top = 55.dp),
                text = product?.title.orEmpty(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = product?.category.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = product?.price.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = productDetailsScreenInfo.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = product?.description.orEmpty(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    maxLines = if (isExpanded.value) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult: TextLayoutResult ->
                        // Check if the text exceeds 2 lines when collapsed
                        showReadMore.value = textLayoutResult.lineCount > 2
                    }
                )

                if (showReadMore.value && !isExpanded.value && product?.description?.isNotEmpty() == true) {
                    Text(
                        text = stringResource(R.string.read_more),
                        color = Color.Blue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { isExpanded.value = !isExpanded.value }
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = productDetailsScreenInfo.reviewText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            ReviewList(
                product?.reviews.orEmpty()
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ProductDetailsScreenContentPreview() {
    val reviews = listOf(
        Review(rating = 5, comment = "Excellent product!", reviewerName = "John Doe", date = "2024-05-23", ratingText = ""),
        Review(rating = 4, comment = "Good, but could be improved.", reviewerName = "Jane Smith", date = "2024-06-12", ratingText = ""),
        Review(rating = 3, comment = "It's okay.", reviewerName = "Alice Johnson", date = "2024-07-01", ratingText = ""),
        Review(rating = 2, comment = "Not as expected.", reviewerName = "Bob Brown", date = "2024-08-15", ratingText = "")
    )

    val product = ProductDetailsContract.State.Data.Product(
        "All",
        id = 5,
        title = "Woman Printed Kurta",
        images = listOf(""),
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = "1500 €",
        reviews = reviews
    )
    ProductDetailsScreenContent(
        product = product,
        productDetailsScreenInfo = ProductDetailsScreenInfo(
            title = stringResource(R.string.product_details_title),
            reviewText = stringResource(R.string.reviews),
            toolBarInfo = ToolBarInfo(
                toolbarLeftIcon = R.drawable.ic_left_arrow,
                toolMiddleIcon = R.drawable.ic_toolbar,
                toolRightIcon = null,
                toolLeftIconVisibility = true,
                toolMiddleIconVisibility = false,
                toolRightIconVisibility = true
            )
        ),
        navigate = {}
    )
}