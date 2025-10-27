package com.example.dinesmart.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.dinesmart.data.RestaurantViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()

    // Read API key from manifest meta-data to show helpful message if missing
    val apiKey: String? = remember {
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            ai.metaData?.getString("com.google.android.geo.API_KEY")
        } catch (e: Exception) {
            null
        }
    }

    // Collect only restaurants that have coordinates
    val coords = remember(restaurantsState) { restaurantsState.mapNotNull { r -> r.lat?.let { lat -> r.lng?.let { lng -> Pair(r, LatLng(lat, lng) ) } } } }

    // Choose initial camera: first restaurant with coords or default coordinate (0,0)
    val initialLatLng = coords.firstOrNull()?.second ?: LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, if (coords.isNotEmpty()) 12f else 2f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            @Suppress("DEPRECATION")
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(8.dp).fillMaxSize()) {
            // If API key is clearly missing or still the placeholder, show an instruction card
            if (apiKey.isNullOrBlank() || apiKey == "YOUR_API_KEY") {
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Maps not configured", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("The Google Maps API key is missing or not set. To display maps, add your API key to AndroidManifest.xml under the com.google.android.geo.API_KEY meta-data entry.")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "See Google Maps Android SDK docs",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                val uri = "https://developers.google.com/maps/documentation/android-sdk/get-api-key".toUri()
                                try { context.startActivity(Intent(Intent.ACTION_VIEW, uri)) } catch (_: Exception) {}
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (coords.isNotEmpty()) {
                // Show map with markers for restaurants - fill available space
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .semantics { contentDescription = "Restaurant location map" },
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = false)
                ) {
                    coords.forEach { (restaurant, latLng) ->
                        Marker(state = MarkerState(position = latLng), title = restaurant.name)
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Tap a marker to open it in Maps.", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    // open external maps centered on first coord
                    val first = coords.firstOrNull()
                    first?.let { (_, latLng) ->
                        val uri = "geo:${latLng.latitude},${latLng.longitude}".toUri()
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            // fallback to browser
                            val web = "https://www.google.com/maps/search/?api=1&query=${latLng.latitude},${latLng.longitude}".toUri()
                            try { context.startActivity(Intent(Intent.ACTION_VIEW, web)) } catch (_: Exception) { }
                        }
                    }
                }) {
                    Text("Open first location in Maps")
                }
            } else {
                // No coordinates available: show helpful message and fallback
                Box(Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
                    Text("No mapped restaurants available. Add coordinates to restaurants to see them on the map.")
                }
            }
        }
    }
}
