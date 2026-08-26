package com.example.shoppinglist.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.dtos.ShoppingListWithProducts
import com.example.shoppinglist.viewmodels.ShoppingListListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListListScreen(
    onBack: () -> Unit,
    onCreateClick: () -> Unit,
    onListClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: ShoppingListListViewModel = hiltViewModel()
) {
    val shoppingLists by viewModel.shoppingLists.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Lists") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(Icons.Default.Add, contentDescription = "Create Shopping List")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                items = shoppingLists,
                key = { it.shoppingList.id }
            ) { listWithProducts ->
                val dismissState = rememberSwipeToDismissBoxState()

                LaunchedEffect(dismissState.currentValue) {
                    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.deleteShoppingList(listWithProducts.shoppingList.id)
                    }
                }

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                                else -> Color.Transparent
                            }, label = "dismissBackground"
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                        ShoppingListItemRow(
                            listWithProducts = listWithProducts,
                            onClick = { onListClick(listWithProducts.shoppingList.id) },
                            onEditClick = { onEditClick(listWithProducts.shoppingList.id) }
                        )
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ShoppingListItemRow(
    listWithProducts: ShoppingListWithProducts,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = listWithProducts.shoppingList.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Created: ${dateFormatter.format(listWithProducts.shoppingList.creationDate)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Items: ${listWithProducts.items.size}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${listWithProducts.shoppingList.status}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}
