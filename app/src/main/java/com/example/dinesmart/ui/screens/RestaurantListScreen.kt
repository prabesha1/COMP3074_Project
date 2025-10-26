package com.example.dinesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dinesmart.data.Restaurant
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.navigation.Routes
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.coerceAtLeast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(navController: NavHostController) {
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()

    val restaurants = if (restaurantsState.isNotEmpty()) restaurantsState else listOf(
        Restaurant(1, "Sushi Place", "Japanese, Sushi", 4),
        Restaurant(2, "Burger Hub", "Fast Food", 5),
        Restaurant(3, "Spice Garden", "Indian Cuisine", 3)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restaurants") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
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
                        .padding(vertical = 6.dp)
                        .semantics { contentDescription = "Restaurant card: ${restaurant.name}" },
                    onClick = { navController.navigate(Routes.detailsRoute(restaurant.id)) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    BoxWithConstraints {
                        val maxW: Dp = this.maxWidth
                        val iconSize = if (maxW < 360.dp) 36.dp else 48.dp

                        Row(Modifier.padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = "${restaurant.name} location", modifier = Modifier.size(iconSize))
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
}
