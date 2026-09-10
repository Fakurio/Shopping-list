package com.example.shoppinglist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.dtos.ProductDto
import com.example.shoppinglist.notifications.NotificationHelper
import com.example.shoppinglist.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    val products: StateFlow<List<ProductDto>> = productRepository
        .getProductList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            notificationHelper.cancelNotification(productId)
            productRepository.deleteById(productId)
        }
    }
}
