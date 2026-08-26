package com.example.shoppinglist.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.dtos.ProductDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductRepository {
    @Insert
    suspend fun insert(product: Product): Long

    @Update
    suspend fun update(product: Product)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM products WHERE id IN (:ids) AND is_tracked = 0")
    suspend fun deleteNonTrackedByIds(ids: List<Int>)

    @Query("""
        SELECT 
            id, name, quantity, 
            last_bought_date AS lastBoughtDate, 
            interval_value AS intervalValue, 
            interval_unit AS intervalUnit,
            active_notification_id AS activeNotificationId,
            is_tracked AS isTracked
        FROM products
        WHERE is_tracked = 1
    """)
    fun getProductList(): Flow<List<ProductDto>>

    @Query("""
        SELECT 
            id, name, quantity, 
            last_bought_date AS lastBoughtDate, 
            interval_value AS intervalValue, 
            interval_unit AS intervalUnit,
            active_notification_id AS activeNotificationId,
            is_tracked AS isTracked
        FROM products
        WHERE id = :id
    """)
    fun getProductById(id: Int): Flow<ProductDto?>
}
