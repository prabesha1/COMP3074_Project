package com.example.dinesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
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
            FloatingActionButton(onClick = { navController.navigate(Routes.ADD) },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = "Add restaurant")
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            items(restaurants) { restaurant ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    onClick = { navController.navigate(Routes.DETAILS) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = "Restaurant location", modifier = Modifier.size(40.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(restaurant.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(restaurant.tags, style = MaterialTheme.typography.bodySmall)
                        }
                        Text("\u2b50 ${restaurant.rating}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
