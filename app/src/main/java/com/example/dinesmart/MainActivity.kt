package com.example.dinesmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.dinesmart.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply Material3 theme
            MaterialTheme {
                // Create a NavController instance
                val navController = rememberNavController()

                // Set up the app's navigation graph within a Surface
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph(navController)
                }
            }
        }
    }
}
