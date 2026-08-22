package com.example.shoppinglist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.enums.ShoppingListStatus

@Entity(tableName = "shopping_lists")
data class ShoppingList (
    @PrimaryKey val id: Int,
    val name: Int,
    val status: ShoppingListStatus
)
