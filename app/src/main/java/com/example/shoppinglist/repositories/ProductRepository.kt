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
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("""
        SELECT 
            id, name, quantity, 
            last_bought_date AS lastBoughtDate, 
            interval_value AS intervalValue, 
            interval_unit AS intervalUnit,
            active_notification_id AS activeNotificationId,
            is_tracked AS isTracked
        FROM products
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
