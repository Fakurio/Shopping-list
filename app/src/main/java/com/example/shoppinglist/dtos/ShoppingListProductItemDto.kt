package com.example.shoppinglist.dtos

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.entities.ShoppingListItem

data class ShoppingListProductItemDto(
    @Embedded val item: ShoppingListItem,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
)
