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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(navController: NavHostController, restaurantId: Int?) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))

    // Load the restaurant details from repository/DB
    LaunchedEffect(restaurantId) {
        restaurantId?.let { vm.loadById(it) }
    }

    val restaurant by vm.selected.collectAsState()

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
            if (restaurant != null) {
                // Header
                Text(restaurant!!.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(12.dp))

                // Details card
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Tags", style = MaterialTheme.typography.titleMedium)
                        Text(restaurant!!.tags, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Rating", style = MaterialTheme.typography.titleMedium)
                        Text("\u2b50 ${restaurant!!.rating}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Address", style = MaterialTheme.typography.titleMedium)
                        Text(restaurant!!.address, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Phone", style = MaterialTheme.typography.titleMedium)
                        Text(restaurant!!.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // In-app map when coordinates available
                if (restaurant!!.lat != null && restaurant!!.lng != null) {
                    val latLng = LatLng(restaurant!!.lat!!, restaurant!!.lng!!)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(latLng, 14f)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .semantics { contentDescription = "Restaurant location map" },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(isMyLocationEnabled = false)
                        ) {
                            Marker(state = MarkerState(position = latLng), title = restaurant!!.name)
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Dial intent
                    Button(onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:${restaurant!!.phone}".toUri()
                        }
                        context.startActivity(dialIntent)
                    }) { Text("Call") }

                    // Open maps with geo:lat,lng?q=label
                    Button(onClick = {
                        val lat = restaurant!!.lat
                        val lng = restaurant!!.lng
                        if (lat != null && lng != null) {
                            val uri = "geo:$lat,$lng?q=$lat,$lng(${Uri.encode(restaurant!!.name)})".toUri()
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            // Fallback if Google Maps isn't installed
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                // Open with any available maps app or browser
                                val uriWeb = "https://www.google.com/maps/search/?api=1&query=$lat,$lng".toUri()
                                val webIntent = Intent(Intent.ACTION_VIEW, uriWeb)
                                context.startActivity(webIntent)
                            }
                        } else {
                            // If no coords, open maps search by address
                            val uriWeb = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(restaurant!!.address)}".toUri()
                            val webIntent = Intent(Intent.ACTION_VIEW, uriWeb)
                            context.startActivity(webIntent)
                        }
                    }) { Text("Open in Maps") }

                    OutlinedButton(onClick = { navController.navigate(Routes.ABOUT) }) { Text("About") }
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Restaurant not found", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
