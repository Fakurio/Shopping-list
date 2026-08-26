package com.example.shoppinglist.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.shoppinglist.dtos.ShoppingListWithProducts
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListRepository {
    @Insert
    suspend fun insert(shoppingList: ShoppingList): Long

    @Transaction
    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    fun getShoppingListWithProductsById(id: Int): Flow<ShoppingListWithProducts?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingListItem>)

    @Delete
    suspend fun deleteItems(items: List<ShoppingListItem>)

    @Query("DELETE FROM shopping_list_items WHERE listId = :listId")
    suspend fun deleteItemsByListId(listId: Int)

    @Update
    suspend fun update(shoppingList: ShoppingList)

    @Query("DELETE FROM products WHERE id IN (:productIds) AND is_tracked = 0")
    suspend fun deleteNonTrackedProducts(productIds: List<Int>)

    @Query("SELECT * FROM shopping_list_items WHERE listId = :listId")
    suspend fun getItemsByListId(listId: Int): List<ShoppingListItem>

    @Delete
    suspend fun delete(shoppingList: ShoppingList)

    @Transaction
    suspend fun deleteShoppingListWithCleanup(listId: Int) {
        val items = getItemsByListId(listId)
        val productIds = items.map { it.productId }

        deleteById(listId)

        if (productIds.isNotEmpty()) {
            deleteNonTrackedProducts(productIds)
        }
    }

    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Transaction
    @Query("SELECT * FROM shopping_lists ORDER BY creationDate DESC")
    fun getShoppingListsWithProducts(): Flow<List<ShoppingListWithProducts>>

    @Transaction
    suspend fun createShoppingListWithItems(shoppingList: ShoppingList, items: List<ShoppingListItem>) {
        val listId = insert(shoppingList).toInt()
        val itemsWithId = items.map { it.copy(listId = listId) }
        insertItems(itemsWithId)
    }

    @Transaction
    suspend fun updateShoppingListWithItems(shoppingList: ShoppingList, items: List<ShoppingListItem>) {
        update(shoppingList)
        val currentItems = getItemsByListId(shoppingList.id)
        
        val toDelete = currentItems.filter { current ->
            items.none { it.productId == current.productId }
        }
        if (toDelete.isNotEmpty()) {
            val productIdsToDelete = toDelete.map { it.productId }
            deleteItems(toDelete)
            deleteNonTrackedProducts(productIdsToDelete)
        }
        insertItems(items)
    }
}
