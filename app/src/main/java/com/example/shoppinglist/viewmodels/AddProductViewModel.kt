package com.example.shoppinglist.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.enums.IntervalUnit
import com.example.shoppinglist.repositories.ProductRepository
import com.example.shoppinglist.ui.ProductFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel(), ProductFormState {

    // Form State
    override var name by mutableStateOf("")
    override var quantity by mutableStateOf("")
    override var intervalValue by mutableStateOf("")
    override var intervalUnit by mutableStateOf(IntervalUnit.DAYS)

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

    fun addProduct() {
        if (!isFormValid) return
        
        viewModelScope.launch {
            val product = Product(
                name = name,
                quantity = quantity.toLong(),
                lastBoughtDate = null,
                intervalValue = intervalValue.toInt(),
                intervalUnit = intervalUnit,
                activeNotificationId = null,
                isTracked = true
            )
            productRepository.insert(product)
            resetForm()
        }
    }

    private fun resetForm() {
        name = ""
        quantity = ""
        intervalValue = ""
        intervalUnit = IntervalUnit.DAYS
    }
}
