package com.example.shoppinglist.entities

import androidx.room3.Embedded
import androidx.room3.Junction
import androidx.room3.Relation

data class ShoppingListWithProducts (
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["id"],
        associateBy = Junction(
            value = ShoppingListItem::class,
            parentColumns = ["listId"],
            entityColumns = ["productId"]
        )
    )
    val products: List<Product>
)
