package gr.android.dummyjson.domain.usecases

import gr.android.dummyjson.domain.repository.LoginRepository
import gr.android.dummyjson.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SilentLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    fun isLoggedIn(): Flow<Boolean> {
        return flow {
            val result = loginRepository.isLoggedIn()
            when(result) {
                is Outcome.Success -> {
                    emit(true)
                }
                else -> {
                    emit(false)
                }
            }
        }
    }
}