package gr.android.dummyjson.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.android.dummyjson.common.annotation.Application
import gr.android.dummyjson.data.local.database.products.ProductsDataSource
import gr.android.dummyjson.data.network.services.ProductsApi
import gr.android.dummyjson.data.repositories.products.ProductsRepositoryImpl
import gr.android.dummyjson.domain.repository.ProductsRepository
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

//    @Provides
//    fun provideProductCategoriesRepository(
//        productsApi: ProductsApi,
//        productCategoriesDao: ProductCategoriesDao,
//        @Application coroutineScope: CoroutineScope,
//    ): ProductCategoriesRepository {
//        return ProductCategoriesRepositoryImpl(
//            productsApi = productsApi,
//            productCategoriesDao = productCategoriesDao,
//            coroutineScope = coroutineScope,
//        )
//    }

    @Provides
    fun provideProductsRepository(
        productsApi: ProductsApi,
        productsDataSource: ProductsDataSource,
        @Application coroutineScope: CoroutineScope,
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            productsApi = productsApi,
            productsDataSource = productsDataSource,
            coroutineScope = coroutineScope,
        )
    }
}
