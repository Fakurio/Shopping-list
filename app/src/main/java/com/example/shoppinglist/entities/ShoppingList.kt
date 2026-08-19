package com.example.shoppinglist.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.shoppinglist.enums.ShoppingListStatus

@Entity(tableName = "shopping_lists")
data class ShoppingList (
    @PrimaryKey val id: Int,
    val name: Int,
    val status: ShoppingListStatus
)
