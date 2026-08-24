package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.ui.ProductDetailScreen
import com.example.shoppinglist.ui.ProductFormScreen
import com.example.shoppinglist.ui.ProductListScreen
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.example.shoppinglist.viewmodels.AddProductViewModel
import com.example.shoppinglist.viewmodels.EditProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object ProductListRoute

@Serializable
data class ProductDetailRoute(val productId: Int)

@Serializable
object AddProductRoute

@Serializable
data class EditProductRoute(val productId: Int)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ProductListRoute,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable<ProductListRoute> {
                        ProductListScreen(
                            onProductClick = { product ->
                                navController.navigate(ProductDetailRoute(product.id))
                            },
                            onAddProductClick = {
                                navController.navigate(AddProductRoute)
                            },
                            onEditProductClick = { productId ->
                                navController.navigate(EditProductRoute(productId))
                            }
                        )
                    }
                    composable<ProductDetailRoute> {
                        ProductDetailScreen(
                            onBack = {
                                navController.popBackStack()
                            },
                            onEditClick = { productId ->
                                navController.navigate(EditProductRoute(productId))
                            }
                        )
                    }
                    composable<AddProductRoute> {
                        val viewModel: AddProductViewModel = hiltViewModel()
                        ProductFormScreen(
                            title = "Add Product",
                            submitButtonText = "Add Product",
                            state = viewModel,
                            onSubmit = {
                                viewModel.addProduct()
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable<EditProductRoute> {
                        val viewModel: EditProductViewModel = hiltViewModel()
                        ProductFormScreen(
                            title = "Edit Product",
                            submitButtonText = "Save Changes",
                            state = viewModel,
                            onSubmit = {
                                viewModel.updateProduct()
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
