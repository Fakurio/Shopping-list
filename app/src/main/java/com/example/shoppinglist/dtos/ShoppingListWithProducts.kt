package com.example.shoppinglist.dtos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem

data class ShoppingListWithProducts (
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ShoppingListItem::class,
            parentColumn = "listId",
            entityColumn = "productId"
        )
    )
    val products: List<Product>
)