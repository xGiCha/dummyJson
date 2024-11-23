package gr.android.dummyjson.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.dummyjson.R
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.domain.usecases.ProductsUseCase
import gr.android.dummyjson.ui.BaseViewModelImpl
import gr.android.dummyjson.utils.Outcome
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
import gr.android.dummyjson.ui.home.HomeContract.State.Data.HomeScreenInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase,
//    private val logoutUseCase: LogoutUseCase,
) : BaseViewModelImpl<HomeContract.State, HomeContract.Event>() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<HomeContract.State?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _productCategories = MutableSharedFlow<List<String>?>(replay = 1)
    val _products = MutableSharedFlow<PagingData<ProductDomainModel>>(replay = 1)
    private val _searchText = MutableStateFlow<String?>("")
    private val _selectedCategory = MutableStateFlow<String?>(All_ITEMS)
    private val _showLogout = MutableStateFlow<Boolean?>(null)

    init {
        refresh()
    }

    fun refresh() {
        getProducts()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val result: StateFlow<HomeContract.State?> =
        combineTuple(
            _errorMessage,
            _products,
//            _showLogout
        ).mapLatest { (error, products,) ->

            val productList = products.map {
                HomeContract.State.Data.Product(
                    category = it.category.orEmpty(),
                    description = it.description.orEmpty(),
                    id = it.id ?: -1,
                    image = it.images?.firstOrNull().orEmpty(),
                    price = (it.price ?: 0.0).toString() + " €",
                    title = it.title.orEmpty()
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
                showLogout = false
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

//    fun getProductCategories() {
//        viewModelScope.launch {
//            categoriesUseCase.invoke().collectLatest {
//                when(it){
//                    is Outcome.Error -> {
//                        _errorMessage.emit(it.message)
//                    }
//                    is Outcome.Loading -> {}
//                    is Outcome.Success -> _productCategories.emit(it.data)
//                }
//            }
//        }
//    }

    private fun getProducts() {
        viewModelScope.launch {
            productsUseCase.invoke().collectLatest {
                when(it) {
                    is Outcome.Error -> error.emit(it.message)
                    is Outcome.Loading -> {}
                    is Outcome.Success -> {
                        _products.emit(it.data)
                    }
                }
            }
        }
    }

//    fun navigateToDetails(productId: Int){
//        events.emitAsync(HomeContract.Event.NavigateToDetailsScreen(productId = productId))
//    }
//
//    fun navigateToLogin(){
//        events.emitAsync(HomeContract.Event.NavigateToLoginScreen)
//    }
//
//    fun showLogout() {
//        viewModelScope.launch {
//            _showLogout.emit(true)
//        }
//    }
//
//    fun hideLogout() {
//        viewModelScope.launch {
//            _showLogout.emit(false)
//        }
//    }
//
//    fun logout() {
//        logoutUseCase.logout()
//    }

    companion object {
        const val All_ITEMS = "All"
    }
}