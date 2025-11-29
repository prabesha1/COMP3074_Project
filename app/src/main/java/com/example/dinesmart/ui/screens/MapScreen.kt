package com.example.dinesmart.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
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
import com.example.dinesmart.ui.components.*
import com.example.dinesmart.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()

    // Animated gradient
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )

    // Read API key from manifest meta-data
    val apiKey: String? = remember {
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            ai.metaData?.getString("com.google.android.geo.API_KEY")
        } catch (e: Exception) {
            null
        }
    }

    // Collect restaurants with coordinates
    val coords = remember(restaurantsState) {
        restaurantsState.mapNotNull { r ->
            r.lat?.let { lat -> r.lng?.let { lng -> Pair(r, LatLng(lat, lng)) } }
        }
    }

    val initialLatLng = coords.firstOrNull()?.second ?: LatLng(43.6532, -79.3832) // Toronto default
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, if (coords.isNotEmpty()) 12f else 10f)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Liquid gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            GradientStart,
                            GradientMiddle,
                            GradientEnd
                        )
                    )
                )
        )

        FloatingGradientOrb(
            color = LiquidPink,
            size = 280.dp,
            initialOffsetX = gradientOffset.dp * 0.1f,
            initialOffsetY = 100.dp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        FloatingGradientOrb(
            color = LiquidTeal,
            size = 240.dp,
            initialOffsetX = (-gradientOffset.dp * 0.08f),
            initialOffsetY = 300.dp,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.1f),
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier.background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    "Map",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            },
                            navigationIcon = {
                                if (navController.previousBackStackEntry != null) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // API Key Missing Warning
                if (apiKey.isNullOrBlank() || apiKey == "YOUR_API_KEY") {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 24.dp,
                        glassAlpha = 0.2f
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Rounded.Warning,
                                contentDescription = null,
                                tint = AccentYellow,
                                modifier = Modifier.size(28.dp)
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Maps Not Configured",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "The Google Maps API key is missing. Add your API key to AndroidManifest.xml to display maps.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                )
                                Surface(
                                    onClick = {
                                        val uri = "https://developers.google.com/maps/documentation/android-sdk/get-api-key".toUri()
                                        try {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                        } catch (_: Exception) {
                                        }
                                    },
                                    modifier = Modifier.padding(top = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        "Get API Key →",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                if (coords.isNotEmpty()) {
                    // Map Card with Glass Effect
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .semantics { contentDescription = "Restaurant location map" },
                        cornerRadius = 28.dp,
                        glassAlpha = 0.18f
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(isMyLocationEnabled = false)
                        ) {
                            coords.forEach { (restaurant, latLng) ->
                                Marker(
                                    state = MarkerState(position = latLng),
                                    title = restaurant.name,
                                    snippet = restaurant.tags
                                )
                            }
                        }
                    }

                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 24.dp,
                        glassAlpha = 0.18f
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.LocationOn,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        "${coords.size} Location${if (coords.size != 1) "s" else ""}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Text(
                                "Tap a marker for details or use the button below to open in Google Maps",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }

                    // Open in Maps Button
                    GlassButton(
                        text = "Open in Google Maps",
                        icon = Icons.Rounded.Map,
                        onClick = {
                            val first = coords.firstOrNull()
                            first?.let { (restaurant, latLng) ->
                                val uri = "geo:${latLng.latitude},${latLng.longitude}?q=${latLng.latitude},${latLng.longitude}(${restaurant.name})".toUri()
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                intent.setPackage("com.google.android.apps.maps")
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    val web = "https://www.google.com/maps/search/?api=1&query=${latLng.latitude},${latLng.longitude}".toUri()
                                    try {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, web))
                                    } catch (_: Exception) {
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = true
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            glassAlpha = 0.25f
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.LocationOff,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    "No Locations Available",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "Add coordinates to restaurants to see them on the map",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
