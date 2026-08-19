package com.example.shoppinglist.repositories

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.shoppinglist.entities.ShoppingListWithProducts

@Dao
interface ShoppingListRepository {
    @Transaction
    @Query("SELECT * FROM shopping_lists")
    suspend fun getShoppingListsWithProducts(): List<ShoppingListWithProducts>
}