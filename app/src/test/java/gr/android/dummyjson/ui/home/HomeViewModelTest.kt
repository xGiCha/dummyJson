package gr.android.dummyjson.ui.home

import android.content.Context
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import gr.android.dummyjson.BaseTest
import gr.android.dummyjson.CoroutineTestRule
import gr.android.dummyjson.domain.usecases.LogoutUseCase
import gr.android.dummyjson.domain.usecases.ProductsUseCase
import gr.android.dummyjson.ui.productDetails.dummyProductDomainModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class HomeViewModelTest: BaseTest() {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var appContext: Context

    @MockK
    lateinit var logoutUseCase: LogoutUseCase

    @MockK
    lateinit var productsUseCase: ProductsUseCase

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        homeViewModel = HomeViewModel(
            appContext = appContext,
            logoutUseCase = logoutUseCase,
            productsUseCase = productsUseCase
        )

        coEvery { productsUseCase.invoke() } returns flowOf (PagingData.from(listOf()))

    }

    @Test
    fun `GIVEN valid products WHEN all products is called THEN get products`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val givePagingData = PagingData.from(giveProductList)

        coEvery { productsUseCase.invoke() } returns flowOf(givePagingData)

        homeViewModel = HomeViewModel(
            appContext = appContext,
            logoutUseCase = logoutUseCase,
            productsUseCase = productsUseCase
        )

        val expected = givePagingData.map {
            HomeContract.State.Data.Product(
                category = " All",
                id = 5,
                title = "Woman Printed Kurta",
                image = "",
                description = "Neque porro quisquam est qui dolorem ipsum quia",
                price = " 1500.0 €",
                brand = ""
            )
        }

        val listExpected = mutableListOf<HomeContract.State.Data.Product>()
        flowOf (expected).map { pagingData ->
            pagingData.map {
                listExpected.add(it)
            }
        }


        homeViewModel.uiModels.test {
            val listActual = mutableListOf<HomeContract.State.Data.Product>()
            (awaitItem() as? HomeContract.State.Data)?.products?.map { pagingData ->
                pagingData.map {
                    listActual.add(it)
                }
            }
            assertEquals(
                expected = listExpected,
                actual = listActual

            )
        }
    }

    @Test
    fun `GIVEN home screen WHEN clicking on item THEN navigate to product details`() = runTest {

        val events = homeViewModel.events.testSubscribe()
        homeViewModel.navigateToDetails(1)
        events.assertLast {
            assertEquals(
                HomeContract.Event.NavigateToDetailsScreen(1),
                it
            )
        }.dispose()
    }

    @Test
    fun `GIVEN home screen WHEN clicking on logout navigate to login screen`() = runTest {

        val events = homeViewModel.events.testSubscribe()
        homeViewModel.navigateToLogin()
        events.assertLast {
            assertEquals(
                HomeContract.Event.NavigateToLoginScreen,
                it
            )
        }.dispose()
    }
}