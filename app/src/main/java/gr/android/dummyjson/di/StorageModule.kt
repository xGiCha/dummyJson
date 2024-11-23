package gr.android.dummyjson.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.android.dummyjson.data.local.database.DummyJsonDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): DummyJsonDatabase {
        return Room.databaseBuilder(
            app,
            DummyJsonDatabase::class.java,
            "products_table.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductsDao(database: DummyJsonDatabase) = database.dao

    @Provides
    @Singleton
    fun provideProductCategoriesDao(database: DummyJsonDatabase) = database.productCategoriesDao
}
