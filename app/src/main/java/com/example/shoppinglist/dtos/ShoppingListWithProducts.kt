package com.example.shoppinglist.dtos

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem

data class ShoppingListWithProducts (
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        entity = ShoppingListItem::class,
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ShoppingListProductItemDto>
)
