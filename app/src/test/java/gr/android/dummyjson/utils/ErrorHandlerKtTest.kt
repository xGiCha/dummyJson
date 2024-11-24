package gr.android.dummyjson.utils

import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.test.runTest

class SafeApiCallTest {

    private val mockSuccessfulResponse: () -> String = { "Success" }
    private val mockNetworkErrorResponse: () -> Unit = { throw IOException("Network error") }
    private val mockHttpErrorResponse: () -> Unit = {
        throw HttpException(mockk(relaxed = true) {
            coEvery { message() } returns "Forbidden"
            coEvery { code() } returns 403
        })
    }
    private val mockUnknownErrorResponse: () -> Unit = { throw Exception("Unknown error") }

    @Test
    fun `GIVEN successful response WHEN API is called THEN return success outcome`() = runTest {
        val result = safeApiCall { mockSuccessfulResponse() }
        assertEquals(Outcome.Success("Success"), result)
    }

    @Test
    fun `GIVEN network error WHEN API is called THEN return network error outcome`() = runTest {
        val result = safeApiCall { mockNetworkErrorResponse() }
        assertEquals(
            Outcome.Error("Network error: Could not reach the server. Check your internet connection."),
            result
        )
    }

    @Test
    fun `GIVEN HTTP error WHEN API is called THEN return HTTP error outcome`() = runTest {
        val result = safeApiCall { mockHttpErrorResponse() }
        assertEquals(
            Outcome.Error("HTTP error: Forbidden (Code: 403)"),
            result
        )
    }

    @Test
    fun `GIVEN unknown error WHEN API is called THEN return unknown error outcome`() = runTest {
        val result = safeApiCall { mockUnknownErrorResponse() }
        assertEquals(
            Outcome.Error("Unknown error: Unknown error"),
            result
        )
    }
}