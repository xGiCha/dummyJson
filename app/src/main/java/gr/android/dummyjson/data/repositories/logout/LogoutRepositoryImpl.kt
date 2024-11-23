package gr.android.dummyjson.data.repositories.logout

import gr.android.dummyjson.data.local.SessionPreferences
import gr.android.dummyjson.domain.repository.LogoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LogoutRepositoryImpl(
    private val sessionPreferences: SessionPreferences,
    private val coroutineScope: CoroutineScope,
): LogoutRepository {

    override suspend fun logout() {
        coroutineScope.launch {
            sessionPreferences.clearTokens()
        }
    }
}