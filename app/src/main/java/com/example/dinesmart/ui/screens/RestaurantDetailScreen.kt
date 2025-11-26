package com.example.dinesmart.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dinesmart.navigation.Routes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.data.maps.DirectionsService
import com.example.dinesmart.data.maps.TravelMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dinesmart.ui.components.*
import com.example.dinesmart.ui.theme.*
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(navController: NavHostController, restaurantId: Int?) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))
    val directionsService = DirectionsService(context)

    val cached = restaurantId?.let { vm.getByIdCached(it) }

    LaunchedEffect(restaurantId) {
        restaurantId?.let {
            vm.loadById(it)
            vm.loadReviews(it)
        }
    }

    val restaurantFromRepo by vm.selected.collectAsState()
    val restaurant = cached ?: restaurantFromRepo

    // Reviews state
    val reviews by vm.reviews.collectAsState()
    val averageRating by vm.averageRating.collectAsState()
    val reviewCount by vm.reviewCount.collectAsState()

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

        // Floating orbs
        FloatingGradientOrb(
            color = LiquidPink,
            size = 280.dp,
            initialOffsetX = gradientOffset.dp * 0.1f,
            initialOffsetY = 100.dp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        FloatingGradientOrb(
            color = LiquidTeal,
            size = 220.dp,
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
                                    "Details",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            },
                            navigationIcon = {
                                if (navController.previousBackStackEntry != null) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.White
                                        )
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
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                restaurant?.let { r ->
                    // Hero Image Card
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 32.dp,
                        glassAlpha = 0.2f
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .clip(RoundedCornerShape(32.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(r.image)
                                    .crossfade(400)
                                    .build(),
                                contentDescription = "${r.name} image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                placeholder = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery),
                                error = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery)
                            )

                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.7f)
                                            )
                                        )
                                    )
                            )

                            // Rating badge
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.3f),
                                shadowElevation = 8.dp,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Box(
                                    modifier = Modifier.background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.35f),
                                                Color.White.copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = AccentYellow,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            "${r.rating}.0",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                            }

                            // Restaurant name
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(24.dp)
                            ) {
                                Text(
                                    text = r.name,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = r.tags,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Details Card
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 28.dp,
                        glassAlpha = 0.18f
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Text(
                                "Information",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            // Address
                            DetailRow(
                                icon = "📍",
                                label = "Address",
                                value = r.address
                            )

                            HorizontalDivider(
                                color = Color.White.copy(alpha = 0.2f),
                                thickness = 1.dp
                            )

                            // Phone
                            DetailRow(
                                icon = "📞",
                                label = "Phone",
                                value = r.phone
                            )

                            HorizontalDivider(
                                color = Color.White.copy(alpha = 0.2f),
                                thickness = 1.dp
                            )

                            // Rating
                            DetailRow(
                                icon = "⭐",
                                label = "Rating",
                                value = "${r.rating} out of 5 stars"
                            )
                        }
                    }

                    // Location Card (if available)
                    if (r.lat != null && r.lng != null) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            cornerRadius = 28.dp,
                            glassAlpha = 0.18f
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "Location",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "Coordinates: ${r.lat}, ${r.lng}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                )
                                Text(
                                    "Tap 'Open in Maps' below to view in an external app",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                )
                            }
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Call Button
                        GlassActionButton(
                            icon = Icons.Rounded.Phone,
                            label = "Call",
                            onClick = {
                                try {
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = "tel:${r.phone}".toUri()
                                    }
                                    context.startActivity(dialIntent)
                                } catch (_: Exception) {
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        // Directions Button
                        GlassActionButton(
                            icon = Icons.Rounded.Directions,
                            label = "Directions",
                            onClick = {
                                r.lat?.let { lat ->
                                    r.lng?.let { lng ->
                                        directionsService.openDirections(
                                            destination = LatLng(lat, lng),
                                            destinationName = r.name
                                        )
                                    }
                                } ?: run {
                                    directionsService.openDirectionsByAddress(r.address)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            isPrimary = true
                        )
                    }

                    // Travel Mode Options
                    if (r.lat != null && r.lng != null) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            cornerRadius = 24.dp,
                            glassAlpha = 0.18f
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "Get Directions",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TravelModeButton(
                                        icon = Icons.Rounded.DirectionsCar,
                                        label = "Drive",
                                        onClick = {
                                            directionsService.openDirectionsWithMode(
                                                destination = LatLng(r.lat!!, r.lng!!),
                                                travelMode = TravelMode.DRIVING
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    TravelModeButton(
                                        icon = Icons.Rounded.DirectionsWalk,
                                        label = "Walk",
                                        onClick = {
                                            directionsService.openDirectionsWithMode(
                                                destination = LatLng(r.lat!!, r.lng!!),
                                                travelMode = TravelMode.WALKING
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    TravelModeButton(
                                        icon = Icons.Rounded.DirectionsTransit,
                                        label = "Transit",
                                        onClick = {
                                            directionsService.openDirectionsWithMode(
                                                destination = LatLng(r.lat!!, r.lng!!),
                                                travelMode = TravelMode.TRANSIT
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // Reviews Section
                    ReviewsSection(
                        reviews = reviews,
                        averageRating = averageRating,
                        reviewCount = reviewCount,
                        onAddReview = { rating, comment ->
                            vm.addReview(r.id, rating, comment)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // About Button
                    GlassButton(
                        text = "About DineSmart",
                        icon = Icons.Rounded.Info,
                        onClick = { navController.navigate(Routes.ABOUT) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                } ?: run {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            modifier = Modifier.padding(24.dp),
                            glassAlpha = 0.25f
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.ErrorOutline,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    "Restaurant not found",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
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

@Composable
private fun DetailRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.25f),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    icon,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun GlassActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = if (isPrimary) 0.3f else 0.2f),
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isPrimary) 0.35f else 0.25f),
                            Color.White.copy(alpha = if (isPrimary) 0.2f else 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun TravelModeButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.2f),
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
