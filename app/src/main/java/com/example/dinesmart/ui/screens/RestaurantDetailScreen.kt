package com.example.dinesmart.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dinesmart.navigation.Routes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.dinesmart.data.RestaurantViewModel
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(navController: NavHostController, restaurantId: Int?) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))

    // Try to obtain cached copy immediately for snappy UI (direct lookup without remember)
    val cached = restaurantId?.let { vm.getByIdCached(it) }

    // Load the restaurant details from repository/DB
    LaunchedEffect(restaurantId) {
        restaurantId?.let { vm.loadById(it) }
    }

    val restaurantFromRepo by vm.selected.collectAsState()
    // prefer cached if available, otherwise the repository-loaded value
    val restaurant = cached ?: restaurantFromRepo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restaurant Details") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            @Suppress("DEPRECATION")
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Make everything defensive: avoid !! and handle nulls gracefully
            restaurant?.let { r ->
                // Header
                Text(r.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(12.dp))

                // Details card
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Tags", style = MaterialTheme.typography.titleMedium)
                        Text(r.tags, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Rating", style = MaterialTheme.typography.titleMedium)
                        Text("\u2b50 ${r.rating}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Address", style = MaterialTheme.typography.titleMedium)
                        Text(r.address, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Phone", style = MaterialTheme.typography.titleMedium)
                        Text(r.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Show a simple, robust location card instead of embedding GoogleMap in composition.
                if (r.lat != null && r.lng != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Restaurant location card" },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Location", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(6.dp))
                            Text("Coordinates: ${r.lat}, ${r.lng}", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                            Text("Tap 'Open in Maps' to view this location in an external Maps app.", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Dial intent
                    Button(onClick = {
                        try {
                            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = "tel:${r.phone}".toUri()
                            }
                            context.startActivity(dialIntent)
                        } catch (_: Exception) {
                            // swallow and show a simple feedback using a snackbar would be better, but keep silent to avoid crash
                        }
                    }) { Text("Call") }

                    // Open maps with geo:lat,lng?q=label or fallback to address search
                    Button(onClick = {
                        try {
                            val lat = r.lat
                            val lng = r.lng
                            if (lat != null && lng != null) {
                                val uri = "geo:$lat,$lng?q=$lat,$lng(${Uri.encode(r.name)})".toUri()
                                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                // Fallback if Google Maps isn't installed
                                if (mapIntent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(mapIntent)
                                } else {
                                    val uriWeb = "https://www.google.com/maps/search/?api=1&query=$lat,$lng".toUri()
                                    val webIntent = Intent(Intent.ACTION_VIEW, uriWeb)
                                    context.startActivity(webIntent)
                                }
                            } else {
                                val uriWeb = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(r.address)}".toUri()
                                val webIntent = Intent(Intent.ACTION_VIEW, uriWeb)
                                context.startActivity(webIntent)
                            }
                        } catch (_: Exception) {
                            // ignore to avoid crashes
                        }
                    }) { Text("Open in Maps") }

                    OutlinedButton(onClick = { navController.navigate(Routes.ABOUT) }) { Text("About") }
                }
            } ?: run {
                // Null state: show friendly fallback
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Restaurant not found", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
