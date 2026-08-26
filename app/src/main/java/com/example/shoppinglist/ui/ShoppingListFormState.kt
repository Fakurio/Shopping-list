package com.example.shoppinglist.ui

import com.example.shoppinglist.dtos.ProductDto
import com.example.shoppinglist.viewmodels.SelectedProduct

import kotlinx.coroutines.flow.StateFlow

interface ShoppingListFormState {
    val listName: String
    val availableProducts: StateFlow<List<ProductDto>>
    val customProductName: String
    val customProductQuantity: String
    val selectedAvailableProduct: ProductDto?
    val availableProductQuantity: String
    val selectedProducts: List<SelectedProduct>

    fun onListNameChange(newName: String)
    fun onCustomProductNameChange(newName: String)
    fun onCustomProductQuantityChange(newQuantity: String)
    fun onSelectedAvailableProductChange(product: ProductDto?)
    fun onAvailableProductQuantityChange(newQuantity: String)
    fun addCustomProductAction()
    fun addExistingProductAction()
    fun removeProduct(product: SelectedProduct)
}
