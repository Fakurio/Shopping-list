package com.example.shoppinglist.repositories

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.shoppinglist.dtos.ShoppingListWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListRepository {
    @Transaction
    @Query("SELECT * FROM shopping_lists")
    fun getShoppingListsWithProducts(): Flow<List<ShoppingListWithProducts>>
}
