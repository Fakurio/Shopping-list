package com.example.shoppinglist.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.dtos.ProductDto
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.entities.ShoppingList
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.repositories.ProductRepository
import com.example.shoppinglist.repositories.ShoppingListRepository
import com.example.shoppinglist.ui.ShoppingListFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditShoppingListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val shoppingListRepository: ShoppingListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ShoppingListFormState {

    private val listId: Int = checkNotNull(savedStateHandle["listId"])
    private var originalList: ShoppingList? = null

    override var listName by mutableStateOf("")
    override var customProductName by mutableStateOf("")
    override var customProductQuantity by mutableStateOf("1")
    override var selectedAvailableProduct by mutableStateOf<ProductDto?>(null)
    override var availableProductQuantity by mutableStateOf("1")

    override val availableProducts: StateFlow<List<ProductDto>> = productRepository
        .getProductList()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProducts = mutableStateListOf<SelectedProduct>()
    override val selectedProducts: List<SelectedProduct> = _selectedProducts

    init {
        viewModelScope.launch {
            shoppingListRepository.getShoppingListWithProductsById(listId).first()?.let { listWithProducts ->
                originalList = listWithProducts.shoppingList
                listName = listWithProducts.shoppingList.name
                _selectedProducts.clear()
                listWithProducts.items.forEach { itemDto ->
                    _selectedProducts.add(
                        SelectedProduct(
                            id = itemDto.product.id,
                            name = itemDto.product.name,
                            quantity = itemDto.item.quantityToBuy
                        )
                    )
                }
            }
        }
    }

    override fun onListNameChange(newName: String) { listName = newName }
    override fun onCustomProductNameChange(newName: String) { customProductName = newName }
    override fun onCustomProductQuantityChange(newQuantity: String) { customProductQuantity = newQuantity }
    override fun onSelectedAvailableProductChange(product: ProductDto?) { selectedAvailableProduct = product }
    override fun onAvailableProductQuantityChange(newQuantity: String) { availableProductQuantity = newQuantity }

    override fun addCustomProductAction() {
        val qty = customProductQuantity.toIntOrNull() ?: 1
        if (customProductName.isNotBlank()) {
            _selectedProducts.add(SelectedProduct(name = customProductName, quantity = qty))
            customProductName = ""
            customProductQuantity = "1"
        }
    }

    override fun addExistingProductAction() {
        val product = selectedAvailableProduct
        val qty = availableProductQuantity.toIntOrNull() ?: 1
        if (product != null) {
            _selectedProducts.add(SelectedProduct(id = product.id, name = product.name, quantity = qty))
            selectedAvailableProduct = null
            availableProductQuantity = "1"
        }
    }

    override fun removeProduct(product: SelectedProduct) {
        _selectedProducts.remove(product)
    }

    fun updateShoppingList(onSuccess: () -> Unit) {
        val currentOriginal = originalList ?: return
        if (listName.isBlank() || _selectedProducts.isEmpty()) return

        viewModelScope.launch {
            val updatedList = currentOriginal.copy(name = listName)
            
            val items = _selectedProducts.map { selected ->
                val productId = if (selected.id == null) {
                    val newProduct = Product(
                        name = selected.name,
                        quantity = selected.quantity.toLong(),
                        lastBoughtDate = null,
                        intervalValue = null,
                        intervalUnit = null,
                        activeNotificationId = null,
                        isTracked = false
                    )
                    productRepository.insert(newProduct).toInt()
                } else {
                    selected.id
                }
                
                ShoppingListItem(
                    productId = productId,
                    listId = listId,
                    quantityToBuy = selected.quantity,
                    isBought = false
                )
            }
            
            shoppingListRepository.updateShoppingListWithItems(updatedList, items)
            onSuccess()
        }
    }
}
