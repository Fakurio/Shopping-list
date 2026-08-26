package com.example.shoppinglist.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.dtos.ProductDto
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.enums.IntervalUnit
import com.example.shoppinglist.repositories.ProductRepository
import com.example.shoppinglist.ui.ProductFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ProductFormState {

    private val productId: Int = checkNotNull(savedStateHandle["productId"])
    private var originalProductDto: ProductDto? = null

    // Form State
    override var name by mutableStateOf("")
    override var quantity by mutableStateOf("")
    override var intervalValue by mutableStateOf("")
    override var intervalUnit by mutableStateOf(IntervalUnit.DAYS)

    init {
        viewModelScope.launch {
            productRepository.getProductById(productId).first()?.let { product ->
                originalProductDto = product
                name = product.name
                quantity = product.quantity.toString()
                intervalValue = product.intervalValue?.toString() ?: ""
                intervalUnit = product.intervalUnit ?: IntervalUnit.DAYS
            }
        }
    }

    // Validation
    private val isNameValid get() = name.isNotBlank()
    private val isQuantityValid get() = (quantity.toLongOrNull() ?: 0L) > 0
    private val isIntervalValid get() = (intervalValue.toIntOrNull() ?: 0) > 0

    override val showNameError get() = !isNameValid && name.isNotEmpty()
    override val showQuantityError get() = !isQuantityValid && quantity.isNotEmpty()
    override val showIntervalError get() = !isIntervalValid && intervalValue.isNotEmpty()

    override val isFormValid get() = isNameValid && isQuantityValid && isIntervalValid

    // Actions
    override fun onNameChange(newValue: String) { name = newValue }
    override fun onQuantityChange(newValue: String) { quantity = newValue }
    override fun onIntervalValueChange(newValue: String) { intervalValue = newValue }
    override fun onIntervalUnitChange(newValue: IntervalUnit) { intervalUnit = newValue }

    fun updateProduct() {
        val currentOriginal = originalProductDto ?: return
        if (!isFormValid) return
        
        viewModelScope.launch {
            val updatedProduct = Product(
                id = currentOriginal.id,
                name = name,
                quantity = quantity.toLong(),
                lastBoughtDate = currentOriginal.lastBoughtDate,
                intervalValue = intervalValue.toInt(),
                intervalUnit = intervalUnit,
                activeNotificationId = currentOriginal.activeNotificationId,
                isTracked = currentOriginal.isTracked
            )
            productRepository.update(updatedProduct)
        }
    }
}
