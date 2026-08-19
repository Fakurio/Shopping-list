package com.example.shoppinglist.entities

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index

@Entity(
    tableName = "shopping_list_items",
    primaryKeys = ["productId", "listId"],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId", "listId")]
)
data class ShoppingListItem(
    val productId: Int,
    val listId: Int,
    @ColumnInfo(name = "quantity_to_buy") val quantityToBuy: Int,
    @ColumnInfo(name = "is_bought") val isBought: Boolean
)
