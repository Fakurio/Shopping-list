package com.example.shoppinglist.repositories

import androidx.room.Dao
import androidx.room.Query
import com.example.shoppinglist.dtos.ProductDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductRepository {
    @Query("""
        SELECT 
            id, name, quantity, 
            last_bought_date AS lastBoughtDate, 
            interval_value AS intervalValue, 
            interval_unit AS intervalUnit 
        FROM products
    """)
    fun getProductList(): Flow<List<ProductDto>>

    @Query("""
        SELECT 
            id, name, quantity, 
            last_bought_date AS lastBoughtDate, 
            interval_value AS intervalValue, 
            interval_unit AS intervalUnit 
        FROM products
        WHERE id = :id
    """)
    fun getProductById(id: Int): Flow<ProductDto?>
}