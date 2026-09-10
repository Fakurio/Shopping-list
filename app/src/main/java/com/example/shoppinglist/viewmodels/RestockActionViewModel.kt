package com.example.shoppinglist.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.dtos.ProductDto
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.enums.ShoppingListStatus
import com.example.shoppinglist.repositories.ProductRepository
import com.example.shoppinglist.repositories.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RestockActionViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val shoppingListRepository: ShoppingListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["productId"])

    val product: StateFlow<ProductDto?> = productRepository
        .getProductById(productId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun addToListAndDismiss(onComplete: () -> Unit) {
        viewModelScope.launch {
            val latestList = shoppingListRepository.getLatestShoppingList()

            if (latestList != null) {
                val existingItem = shoppingListRepository.getItemByListAndProductId(latestList.id, productId)
                
                val itemToInsert = existingItem?.copy(quantityToBuy = existingItem.quantityToBuy + 1)
                    ?: ShoppingListItem(
                        productId = productId,
                        listId = latestList.id,
                        quantityToBuy = 1,
                        isBought = false
                    )
                shoppingListRepository.insertItems(listOf(itemToInsert))
            } else {
                val currentDate = Date()
                val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate = dateFormatter.format(currentDate)

                val newList = ShoppingList(
                    name = "Restock List_$formattedDate",
                    status = ShoppingListStatus.ACTIVE,
                    creationDate = currentDate
                )
                val item = ShoppingListItem(
                    productId = productId,
                    listId = 0,
                    quantityToBuy = 1,
                    isBought = false
                )
                shoppingListRepository.createShoppingListWithItems(newList, listOf(item))
            }
            onComplete()
        }
    }
}
