package com.example.shoppinglist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListFormScreen(
    title: String,
    submitButtonText: String,
    state: ShoppingListFormState,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    val availableProducts by state.availableProducts.collectAsState()

    val filteredProducts = remember(availableProducts, state.selectedProducts.size) {
        val selectedIds = state.selectedProducts.mapNotNull { it.id }.toSet()
        availableProducts.filter { it.id !in selectedIds }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = state.listName,
                    onValueChange = { state.onListNameChange(it) },
                    label = { Text("Shopping List Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Select Tracked Product", style = MaterialTheme.typography.titleMedium)
                var expanded by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = state.selectedAvailableProduct?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Product") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                ),
                            placeholder = { Text("Pick from list") }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredProducts.forEach { product ->
                                DropdownMenuItem(
                                    text = { Text(product.name) },
                                    onClick = {
                                        state.onSelectedAvailableProductChange(product)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = state.availableProductQuantity,
                        onValueChange = { state.onAvailableProductQuantityChange(it) },
                        label = { Text("Qty") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(70.dp)
                    )
                    IconButton(onClick = {
                        state.addExistingProductAction()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Tracked")
                    }
                }
            }

            item {
                Text("Add Custom Product", style = MaterialTheme.typography.titleMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.customProductName,
                        onValueChange = { state.onCustomProductNameChange(it) },
                        label = { Text("Product Name") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Enter name") }
                    )
                    OutlinedTextField(
                        value = state.customProductQuantity,
                        onValueChange = { state.onCustomProductQuantityChange(it) },
                        label = { Text("Qty") },
                        placeholder = { Text("1") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(70.dp)
                    )
                    IconButton(onClick = {
                        state.addCustomProductAction()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Custom")
                    }
                }
            }
            
            item {
                Text("Selected Items", style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
            }

            items(state.selectedProducts) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.name.trim(),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Quantity: ${item.quantity}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = { state.removeProduct(item) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                    }
                }
            }

            item {
                Button(
                    onClick = onSubmit,
                    enabled = state.listName.isNotBlank() && state.selectedProducts.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(submitButtonText)
                }
            }
        }
    }
}
