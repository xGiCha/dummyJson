package gr.android.dummyjson.ui.productDetails

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import gr.android.dummyjson.R
import gr.android.dummyjson.common.annotation.Application
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.domain.usecases.ProductDetailsUseCase
import gr.android.dummyjson.ui.BaseViewModelImpl
import gr.android.dummyjson.ui.emitAsync
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.ProductDetailsScreenInfo
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo
import gr.android.dummyjson.utils.Outcome
import gr.android.dummyjson.utils.formatDate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val productDetailsUseCase: ProductDetailsUseCase,
) : BaseViewModelImpl<ProductDetailsContract.State, ProductDetailsContract.Event>() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<ProductDetailsContract.State?>(null)
    private val _product = MutableSharedFlow<ProductDomainModel?>(replay = 1)

    private val result: StateFlow<ProductDetailsContract.State?> =
        combine(
            _product,
        ) { (product) ->

            ProductDetailsContract.State.Data(
                productDetailsScreenInfo = ProductDetailsScreenInfo(
                    title = appContext.getString(R.string.product_details_title),
                    reviewText = appContext.getString(R.string.reviews),
                    toolBarInfo = ToolBarInfo(
                        toolbarLeftIcon = R.drawable.ic_left_arrow,
                        toolMiddleIcon = R.drawable.ic_toolbar,
                        toolRightIcon = -1,
                        toolLeftIconVisibility = true,
                        toolMiddleIconVisibility = false,
                        toolRightIconVisibility = false
                    )
                ),
                product = ProductDetailsContract.State.Data.Product (
                    category = "${appContext.getString(R.string.category)} ${product?.category.orEmpty()}",
                    description = product?.description.orEmpty(),
                    id = product?.id ?: -1,
                    images = product?.images.orEmpty(),
                    price = "${appContext.getString(R.string.price)} ${(product?.price ?: 0.0).toString() + " €"}",
                    title = product?.title.orEmpty(),
                    reviews = product?.reviews?.map { review ->
                        ProductDetailsContract.State.Data.Product.Review(
                            ratingText = "${appContext.getString(R.string.rating)} ${review.rating}",
                            rating = review.rating,
                            date = "${appContext.getString(R.string.date)} ${review.date?.formatDate() ?: appContext.getString(R.string.no_date)}",
                            reviewerName = "${appContext.getString(R.string.reviewer)} ${review.reviewerName ?: appContext.getString(R.string.anonymous)}",
                            comment = review.comment ?: appContext.getString(R.string.no_comments),
                        )
                    }.orEmpty(),
                ),
            )
        }.onEach {
            isLoading.value = false
            error.value = null
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override val uiState: StateFlow<ProductDetailsContract.State?>
        get() = combine(isLoading, result, error) { isLoading, result, error ->
            val response = if (error != null) {
                ProductDetailsContract.State.Error(error)
            } else if (isLoading) {
                ProductDetailsContract.State.Loading
            } else {
                result ?: ProductDetailsContract.State.Error("No info retrieved")
            }
            lastState.value = response
            response
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            lastState.value
        )

    fun getProduct(productId: Int) {
        viewModelScope.launch {
            productDetailsUseCase.invoke(productId).collectLatest {
                when(it){
                    is Outcome.Error -> {
                        error.emit(it.message)
                    }
                    is Outcome.Loading -> {}
                    is Outcome.Success -> _product.emit(it.data)
                }
            }
        }
    }

    fun onBack(){
        events.emitAsync(ProductDetailsContract.Event.OnBack)
    }

}