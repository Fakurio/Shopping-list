package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.shoppinglist.activities.ProductListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, ProductListActivity::class.java))
        finish()
    }
}