package com.example.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglist.database.AppDatabase
import com.example.shoppinglist.repositories.ProductRepository
import com.example.shoppinglist.repositories.ShoppingListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shopping_list_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideShoppingListRepository(database: AppDatabase): ShoppingListRepository {
        return database.shoppingListRepository()
    }

    @Provides
    fun provideProductRepository(database: AppDatabase): ProductRepository {
        return database.productRepository()
    }
}
