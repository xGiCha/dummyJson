package gr.android.dummyjson.domain.usecases

import gr.android.dummyjson.common.annotation.Application
import gr.android.dummyjson.domain.repository.LogoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val logoutRepository: LogoutRepository,
    @Application private val coroutineScope: CoroutineScope,
//    private val productsDataSource: ProductsDataSource
) {

    fun logout() {
        coroutineScope.launch {
            logoutRepository.logout()
//            productsDataSource.clearTable()
        }
    }
}