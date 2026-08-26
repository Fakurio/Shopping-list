package com.example.shoppinglist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.enums.ShoppingListStatus
import java.util.Date

@Entity(tableName = "shopping_lists")
data class ShoppingList (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val status: ShoppingListStatus,
    val creationDate: Date
)
