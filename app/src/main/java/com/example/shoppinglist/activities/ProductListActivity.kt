package com.example.shoppinglist.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.ui.ProductDetailScreen
import com.example.shoppinglist.ui.ProductListScreen
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object ProductListRoute

@Serializable
data class ProductDetailRoute(val productId: Int)

@AndroidEntryPoint
class ProductListActivity : ComponentActivity() {
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
                            }
                        )
                    }
                    composable<ProductDetailRoute> {
                        ProductDetailScreen(
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
