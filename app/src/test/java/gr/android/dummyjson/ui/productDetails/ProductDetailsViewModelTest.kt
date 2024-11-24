package gr.android.dummyjson.ui.productDetails

import gr.android.dummyjson.R
import android.content.Context
import app.cash.turbine.test
import gr.android.dummyjson.BaseTest
import gr.android.dummyjson.CoroutineTestRule
import gr.android.dummyjson.domain.uiModels.ProductDomainModel
import gr.android.dummyjson.domain.usecases.ProductDetailsUseCase
import gr.android.dummyjson.utils.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class ProductDetailsViewModelTest: BaseTest() {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var productDetailsUseCase: ProductDetailsUseCase

    @MockK
    lateinit var appContext: Context

    private lateinit var productDetailsViewModel: ProductDetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        productDetailsViewModel = ProductDetailsViewModel(
            appContext = appContext,
            productDetailsUseCase = productDetailsUseCase
        )

        coEvery { productDetailsUseCase.invoke(1) } returns flowOf (Outcome.Success(dummyProductDomainModel))
    }

    @Test
    fun `GIVEN valid id WHEN product is selected THEN get product details`() = runTest {
        val giveProductList = dummyProductDomainModel

        coEvery { productDetailsUseCase.invoke(1) } returns flowOf(Outcome.Success(giveProductList))

        productDetailsViewModel.getProduct(1)

        productDetailsViewModel.uiModels.test {
            assertEquals(
                expected = dummyProductState,
                actual = (awaitItem() as? ProductDetailsContract.State.Data)?.product

            )
        }
    }

    @Test
    fun `GIVEN invalid product WHEN product is selected THEN get error`() = runTest {

        coEvery { productDetailsUseCase.invoke(1) } returns flowOf(Outcome.Error("Error"))

        productDetailsViewModel.getProduct(1)

        productDetailsViewModel.uiModels.test {
            assertEquals(
                expected = "Error",
                actual = (awaitItem() as? ProductDetailsContract.State.Error)?.value

            )
        }
    }

    @Test
    fun `GIVEN valid products WHEN product is selected THEN product details info`() = runTest {
        val giveProductList = dummyProductDomainModel
        val givenProductDetailsScreenInfo = dummyProductDetailsScreenInfo

        coEvery { productDetailsUseCase.invoke(1) } returns flowOf(Outcome.Success(giveProductList))

        productDetailsViewModel.getProduct(1)

        productDetailsViewModel.uiModels.test {
            assertEquals(
                expected = givenProductDetailsScreenInfo,
                actual = (awaitItem() as? ProductDetailsContract.State.Data)?.productDetailsScreenInfo

            )
        }
    }

    @Test
    fun `GIVEN product details WHEN pressing back THEN navigate to home screen`() = runTest {

        val events = productDetailsViewModel.events.testSubscribe()
        productDetailsViewModel.onBack()
        events.assertLast {
            assertEquals(
                ProductDetailsContract.Event.OnBack,
                it
            )
        }.dispose()
    }
}

val dummyProductDomainModel =
    ProductDomainModel(
        category = "All",
        id = 5,
        title = "Woman Printed Kurta",
        images = listOf(),
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = 1500.0,
        reviews = listOf()
    )

val dummyProductState = ProductDetailsContract.State.Data.Product (
    category = " All",
    id = 5,
    title = "Woman Printed Kurta",
    images = listOf(),
    description = "Neque porro quisquam est qui dolorem ipsum quia",
    price = " 1500.0 €",
    reviews = listOf()
)

val dummyProductDetailsScreenInfo = ProductDetailsContract.State.Data.ProductDetailsScreenInfo(
    title = "",
    reviewText = "",
    toolBarInfo = ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo(
        toolbarLeftIcon = R.drawable.ic_left_arrow,
        toolMiddleIcon = R.drawable.ic_toolbar,
        toolRightIcon = -1,
        toolLeftIconVisibility = true,
        toolMiddleIconVisibility = false,
        toolRightIconVisibility = false,
    )
)