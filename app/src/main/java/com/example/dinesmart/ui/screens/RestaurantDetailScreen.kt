package com.example.dinesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dinesmart.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(navController: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Restaurant Details") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("🍔 Burger Hub", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Address: 123 Main Street, Toronto")
            Text("Phone: (416) 555-7890")
            Text("Rating: ⭐⭐⭐⭐☆ (4.0)")
            Spacer(Modifier.height(12.dp))
            Button(onClick = { navController.navigate(Routes.MAP) }) { Text("Get Directions") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Routes.ABOUT) }) { Text("Share Info") }
        }
    }
}
