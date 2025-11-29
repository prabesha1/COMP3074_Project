package com.example.dinesmart.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dinesmart.ui.components.*
import com.example.dinesmart.ui.theme.*
import androidx.compose.foundation.layout.FlowRow

private fun getDefaultRestaurantImage(id: Long): String {
    val images = listOf(
        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c",
        "https://images.unsplash.com/photo-1550547660-d9450f859349",
        "https://images.unsplash.com/photo-1512058564366-18510be2db19",
        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4",
        "https://images.unsplash.com/photo-1555396273-367ea4eb4db5",
        "https://images.unsplash.com/photo-1559339352-11d035aa65de",
        "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9",
        "https://images.unsplash.com/photo-1565299585323-38d6b0865b47"
    )
    return images[(id % images.size).toInt()]
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(navController: NavHostController) {
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()
    val filteredRestaurants by vm.filteredRestaurants.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val selectedCuisine by vm.selectedCuisineFilter.collectAsState()
    val minRating by vm.minRatingFilter.collectAsState()

    var showFilters by remember { mutableStateOf(false) }
    val cuisines = remember(restaurantsState) { vm.getAllCuisines() }

    val restaurants = if (searchQuery.isNotEmpty() || selectedCuisine != null || minRating > 0) {
        filteredRestaurants.map { restaurant ->
            if (restaurant.image.isNullOrEmpty()) {
                restaurant.copy(image = getDefaultRestaurantImage(restaurant.id.toLong()))
            } else {
                restaurant
            }
        }
    } else {
        if (restaurantsState.isNotEmpty()) {
            restaurantsState.map { restaurant ->
                if (restaurant.image.isNullOrEmpty()) {
                    restaurant.copy(image = getDefaultRestaurantImage(restaurant.id.toLong()))
                } else {
                    restaurant
                }
            }
        } else {
            listOf(
                Restaurant(1, "Sushi Place", "Japanese, Sushi", 4, address = "123 Ocean Ave, Vancouver, BC", phone = "+1-604-555-0100", image = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c"),
                Restaurant(2, "Burger Hub", "Fast Food", 5, address = "45 King St W, Toronto, ON", phone = "+1-416-555-0123", image = "https://images.unsplash.com/photo-1550547660-d9450f859349"),
                Restaurant(3, "Spice Garden", "Indian Cuisine", 3, address = "88 Queen St, Ottawa, ON", phone = "+1-613-555-0145", image = "https://images.unsplash.com/photo-1512058564366-18510be2db19")
            )
        }
    }

    // Animated gradient background
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

        // Floating orbs for liquid effect
        FloatingGradientOrb(
            color = LiquidPink,
            size = 300.dp,
            initialOffsetX = gradientOffset.dp * 0.1f,
            initialOffsetY = 50.dp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        FloatingGradientOrb(
            color = LiquidTeal,
            size = 250.dp,
            initialOffsetX = (-gradientOffset.dp * 0.08f),
            initialOffsetY = 200.dp,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        FloatingGradientOrb(
            color = LiquidOrange,
            size = 280.dp,
            initialOffsetX = gradientOffset.dp * 0.06f,
            initialOffsetY = (-50).dp,
            modifier = Modifier.align(Alignment.BottomStart)
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
                                    "Restaurants",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            },
                            navigationIcon = {
                            if (navController.previousBackStackEntry != null) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back", tint = Color.White)
                                }
                            }
                            },
                            actions = {
                                IconButton(onClick = { showFilters = !showFilters }) {
                                    Icon(
                                        if (showFilters) Icons.Rounded.FilterListOff else Icons.Rounded.FilterList,
                                        contentDescription = "Toggle filters",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = Color.White
                            )
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.ADD) },
                    containerColor = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(18.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Restaurant", tint = Color.White)
                    }
                }
            }
        ) { padding ->
            if (restaurants.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(24.dp),
                        glassAlpha = 0.25f
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Restaurant,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                "No Restaurants Yet",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                "Add your first restaurant to get started",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Search Bar
                    item {
                        GlassSearchBar(
                            query = searchQuery,
                            onQueryChange = { vm.setSearchQuery(it) },
                            onSearch = { },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Filter Section
                    if (showFilters) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Cuisine Filters
                                if (cuisines.isNotEmpty()) {
                                    Text(
                                        "Cuisine Type",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(start = 4.dp)
                                    )

                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        cuisines.take(10).forEach { cuisine ->
                                            FilterChip(
                                                selected = selectedCuisine == cuisine,
                                                onClick = {
                                                    vm.setCuisineFilter(
                                                        if (selectedCuisine == cuisine) null else cuisine
                                                    )
                                                },
                                                label = cuisine
                                            )
                                        }
                                    }
                                }

                                // Rating Filter
                                RatingFilterBar(
                                    selectedRating = minRating,
                                    onRatingSelected = { vm.setMinRatingFilter(it) },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Clear all filters button
                                if (searchQuery.isNotEmpty() || selectedCuisine != null || minRating > 0) {
                                    GlassButton(
                                        text = "Clear All Filters",
                                        icon = Icons.Rounded.Clear,
                                        onClick = { vm.clearFilters() },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    // Results count
                    if (searchQuery.isNotEmpty() || selectedCuisine != null || minRating > 0) {
                        item {
                            Text(
                                "Found ${restaurants.size} restaurant${if (restaurants.size != 1) "s" else ""}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    items(restaurants, key = { it.id }) { restaurant ->
                        LiquidGlassRestaurantCard(
                            restaurant = restaurant,
                            onClick = { navController.navigate(Routes.detailsRoute(restaurant.id)) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LiquidGlassRestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .semantics { contentDescription = "Restaurant card: ${restaurant.name}" },
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            Column {
                // Hero Image Section with Glass Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(restaurant.image)
                            .crossfade(400)
                            .build(),
                        contentDescription = "${restaurant.name} image",
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
                                        Color.Black.copy(alpha = 0.75f)
                                    ),
                                    startY = 100f
                                )
                            )
                    )

                    // Glass Rating Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.25f),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Box(
                            modifier = Modifier.background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0.15f)
                                    )
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AccentYellow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    "${restaurant.rating}.0",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Restaurant Info Overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = restaurant.name,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = restaurant.tags,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Details Section with Glass Effect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.12f),
                                    Color.White.copy(alpha = 0.05f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Address
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📍", style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                            Text(
                                text = restaurant.address,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.95f)
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Phone
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📞", style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                            Text(
                                text = restaurant.phone,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Glass Action Button
                        Surface(
                            onClick = onClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White.copy(alpha = 0.3f),
                            shadowElevation = 6.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.35f),
                                                Color.White.copy(alpha = 0.2f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Rounded.Visibility,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        "View Details",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
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
}

