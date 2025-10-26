package com.example.dinesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dinesmart.navigation.Routes

// Data class representing a restaurant
data class Restaurant(val name: String, val tags: String, val rating: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(navController: NavHostController) {
    val restaurants = listOf(
        Restaurant("Sushi Place", "Japanese, Sushi", 4),
        Restaurant("Burger Hub", "Fast Food", 5),
        Restaurant("Spice Garden", "Indian Cuisine", 3)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Restaurants") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.ADD) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            items(restaurants) { restaurant ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    onClick = { navController.navigate(Routes.DETAILS) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(restaurant.name, style = MaterialTheme.typography.titleMedium)
                        Text(restaurant.tags, style = MaterialTheme.typography.bodySmall)
                        Text("\u2b50 Rating: ${restaurant.rating}/5")
                    }
                }
            }
        }
    }
}
