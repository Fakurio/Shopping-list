package com.example.shoppinglist.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.dtos.ProductDto
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.enums.IntervalUnit
import com.example.shoppinglist.notifications.NotificationHelper
import com.example.shoppinglist.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val notificationHelper: NotificationHelper,
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

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val currentProductDto = product.value ?: return@launch
            
            val updatedDto = currentProductDto.copy(notificationsEnabled = enabled)

            val entity = Product(
                id = updatedDto.id,
                name = updatedDto.name,
                quantity = updatedDto.quantity,
                lastBoughtDate = updatedDto.lastBoughtDate,
                intervalValue = updatedDto.intervalValue,
                intervalUnit = updatedDto.intervalUnit,
                isTracked = updatedDto.isTracked,
                notificationsEnabled = enabled
            )

            if (enabled) {
                notificationHelper.scheduleNotification(
                    entity.id,
                    entity.name,
                    entity.intervalValue ?: 0,
                    entity.intervalUnit ?: IntervalUnit.DAYS
                )
            } else {
                notificationHelper.cancelNotification(entity.id)
            }
            productRepository.update(entity)
        }
    }

    fun deleteProduct() {
        viewModelScope.launch {
            notificationHelper.cancelNotification(productId)
            productRepository.deleteById(productId)
        }
    }
}
