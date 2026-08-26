package com.example.shoppinglist.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.dtos.ShoppingListWithProducts
import com.example.shoppinglist.repositories.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListDetailViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: Int = checkNotNull(savedStateHandle["listId"])

    val shoppingList: StateFlow<ShoppingListWithProducts?> = shoppingListRepository
        .getShoppingListWithProductsById(listId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun deleteShoppingList() {
        viewModelScope.launch {
            shoppingListRepository.deleteShoppingListWithCleanup(listId)
        }
    }
}
