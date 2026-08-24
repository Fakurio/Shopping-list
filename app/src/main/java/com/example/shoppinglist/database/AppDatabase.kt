package com.example.shoppinglist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.repositories.ProductRepository
import com.example.shoppinglist.repositories.ShoppingListRepository

@Database(entities = [Product::class, ShoppingList::class, ShoppingListItem::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListRepository(): ShoppingListRepository
    abstract fun productRepository(): ProductRepository
}
