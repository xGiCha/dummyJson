package gr.android.dummyjson.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.android.dummyjson.common.annotation.Application
import gr.android.dummyjson.data.local.database.DummyJsonDatabase
import gr.android.dummyjson.data.local.database.products.ProductsDataSource
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.data.repositories.products.ProductDetailsRepositoryImpl
import gr.android.dummyjson.data.repositories.products.ProductsRepositoryImpl
import gr.android.dummyjson.domain.repository.ProductDetailsRepository
import gr.android.dummyjson.domain.repository.ProductsRepository
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    fun provideProductDetailsRepository(
        productsApi: ProductsApi,
    ): ProductDetailsRepository {
        return ProductDetailsRepositoryImpl(
            productsApi = productsApi,
        )
    }

    @Provides
    fun provideProductsRepository(
        productsApi: ProductsApi,
        productsDataSource: DummyJsonDatabase,
        @Application coroutineScope: CoroutineScope,
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            productsApi = productsApi,
            productsDataSource = productsDataSource,
            coroutineScope = coroutineScope,
        )
    }
}
