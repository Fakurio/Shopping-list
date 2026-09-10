package com.example.shoppinglist.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.dtos.formattedInterval
import com.example.shoppinglist.viewmodels.ProductDetailViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onEditClick: (Int) -> Unit = {}
) {
    val product by viewModel.product.collectAsState()
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    product?.let {
                        IconButton(onClick = { onEditClick(it.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { 
                            viewModel.deleteProduct()
                            onBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val currentProduct = product
            if (currentProduct != null) {
                Text(
                    text = currentProduct.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Quantity: ${currentProduct.quantity}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                val lastBoughtText = currentProduct.lastBoughtDate?.let { dateFormatter.format(it) } ?: "Never"
                Text(
                    text = "Last Bought: $lastBoughtText",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Text(
                    text = "Interval: ${currentProduct.formattedInterval}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Remind me when it's time to restock",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = currentProduct.notificationsEnabled,
                        onCheckedChange = { enabled ->
                            viewModel.toggleNotifications(enabled)
                        }
                    )
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
