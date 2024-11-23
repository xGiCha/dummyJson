package gr.android.dummyjson.ui.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import gr.android.dummyjson.R
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.domain.usecases.LogoutUseCase
import gr.android.dummyjson.domain.usecases.ProductsUseCase
import gr.android.dummyjson.ui.BaseViewModelImpl
import gr.android.dummyjson.ui.emitAsync
import gr.android.dummyjson.ui.home.HomeContract.State.Data.HomeScreenInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val productsUseCase: ProductsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModelImpl<HomeContract.State, HomeContract.Event>() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<HomeContract.State?>(null)
    private val _products = MutableSharedFlow<PagingData<ProductDomainModel>>(replay = 1)
    private val _showLogout = MutableStateFlow<Boolean?>(null)

    init {
        refresh()
    }

    fun refresh() {
        getProducts()
    }

    private val result: StateFlow<HomeContract.State?> =
        combine(
            _products,
            _showLogout
        ) { products, showLogout ->

            val productList = products.map {
                HomeContract.State.Data.Product(
                    category = it.category.orEmpty(),
                    description = it.description.orEmpty(),
                    id = it.id,
                    image = it.images?.firstOrNull().orEmpty(),
                    price = "${appContext.getString(R.string.price)} ${(it.price ?: 0.0).toString() + " €"}",
                    title = it.title.orEmpty(),
                    brand = "${appContext.getString(R.string.brand)} ${it.brand.orEmpty()}",
                )
            }

            HomeContract.State.Data(
                homeScreenInfo = HomeScreenInfo(
                    allFeaturedTitle = "All Featured",
                    toolbarInfo = HomeScreenInfo.ToolBarInfo(
                        toolbarLeftIcon = R.drawable.ic_left_arrow,
                        toolMiddleIcon = R.drawable.ic_toolbar,
                        toolRightIcon = R.drawable.ic_profile,
                        toolLeftIconVisibility = false,
                        toolMiddleIconVisibility = true,
                        toolRightIconVisibility = true
                    )
                ),
                products = flowOf(productList),
                showLogout = showLogout ?: false
            )
        }.onEach {
            isLoading.value = false
            error.value = null
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override val uiState: StateFlow<HomeContract.State?>
        get() = combine(isLoading, result, error) { isLoading, result, error ->
            val response = if (error != null) {
                HomeContract.State.Error(error)
            } else if (isLoading) {
                HomeContract.State.Loading
            } else {
                result
            }
            lastState.value = response
            response
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            lastState.value
        )

    private fun getProducts() {
        viewModelScope.launch {
            productsUseCase.invoke().collectLatest {
                _products.emit(it)
            }
        }
    }

    fun navigateToDetails(productId: Int){
        events.emitAsync(HomeContract.Event.NavigateToDetailsScreen(productId = productId))
    }

    fun navigateToLogin(){
        events.emitAsync(HomeContract.Event.NavigateToLoginScreen)
    }

    fun showLogout() {
        viewModelScope.launch {
            _showLogout.emit(true)
        }
    }

    fun hideLogout() {
        viewModelScope.launch {
            _showLogout.emit(false)
        }
    }

    fun logout() {
        logoutUseCase.logout()
    }

    fun setErrorMessage(errorMessage: String) {
        viewModelScope.launch {
            error.emit(errorMessage)
        }
    }
}