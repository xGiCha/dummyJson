package gr.android.dummyjson.data.network

import androidx.paging.PagingSource
import gr.android.dummyjson.data.network.models.products.ProductDTO
import gr.android.dummyjson.data.network.models.products.ProductsDTO
import gr.android.dummyjson.data.network.services.ProductsApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProductsPagingDatasourceTest {

    @MockK
    private lateinit var api: ProductsApi

    private lateinit var datasource: ProductsPagingDatasource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        datasource = ProductsPagingDatasource(api)
    }

    @Test
    fun `load returns page when successful response`() = runBlocking {
        // Arrange
        val productList = listOf(
            ProductDTO(
                id = 1, title = "Product 1", description = "Description 1", category = "Category 1",
                price = 10.0, discountPercentage = 5.0, rating = 4.5, stock = 100, tags = listOf(),
                brand = "Brand", sku = "SKU", dimensions = ProductDTO.Dimensions(10.0, 20.0, 5.0),
                warrantyInformation = "Warranty", shippingInformation = "Shipping Info",
                availabilityStatus = "Available", reviews = listOf(), returnPolicy = "Policy",
                minimumOrderQuantity = 1, meta = ProductDTO.Meta("date", "date", "barcode", null),
                thumbnail = null, images = listOf()
            )
        )
        val productsResponse = ProductsDTO(products = productList, total = 100, skip = 0, limit = 20)

        // Mocking the API response
        coEvery { api.getProducts(limit = 20, skip = 0) } returns productsResponse

        // Act
        val result = datasource.load(PagingSource.LoadParams.Refresh(1, 20, false))

        // Assert
        val expected = PagingSource.LoadResult.Page(
            data = productList,
            prevKey = null,
            nextKey = 2
        )
        assertEquals(expected, result)
    }

    @Test
    fun `load returns error when IOException occurs`() = runBlocking {
        // Arrange
        coEvery { api.getProducts(limit = 20, skip = 0) } throws IOException("Network error")

        // Act
        val result = datasource.load(PagingSource.LoadParams.Refresh(1, 20, false))

        // Assert
        assert(result is PagingSource.LoadResult.Error)
        assert((result as PagingSource.LoadResult.Error).throwable is IOException)
    }
}