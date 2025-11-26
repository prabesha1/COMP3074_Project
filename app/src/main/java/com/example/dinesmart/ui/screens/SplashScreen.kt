package com.example.dinesmart.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.animateContentSize
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.blur
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dinesmart.R
import com.example.dinesmart.data.Restaurant
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.navigation.Routes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.draw.rotate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()

    val featured: List<Restaurant> = if (restaurantsState.isNotEmpty()) restaurantsState.take(6) else listOf(
        Restaurant(1, "Sushi Place", "Japanese • Sushi", 4, address = "123 Ocean Ave, Vancouver, BC", phone = "+1-604-555-0100", image = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c"),
        Restaurant(2, "Burger Hub", "Fast Food", 5, address = "45 King St W, Toronto, ON", phone = "+1-416-555-0123", image = "https://images.unsplash.com/photo-1550547660-d9450f859349"),
        Restaurant(3, "Spice Garden", "Indian", 3, address = "88 Queen St, Ottawa, ON", phone = "+1-613-555-0145", image = "https://images.unsplash.com/photo-1512058564366-18510be2db19"),
        Restaurant(4, "Pasta Corner", "Italian", 4, address = "67 Main St, Montreal, QC", phone = "+1-514-555-0167", image = "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9"),
        Restaurant(5, "Taco Town", "Mexican", 5, address = "99 Bay St, Calgary, AB", phone = "+1-403-555-0199", image = "https://images.unsplash.com/photo-1565299585323-38d6b0865b47"),
        Restaurant(6, "BBQ Palace", "American BBQ", 4, address = "234 Elm St, Edmonton, AB", phone = "+1-780-555-0234", image = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1")
    )

    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Dynamic mesh gradient colors (iOS 18 style)
    val dynamicGradient = Brush.verticalGradient(
        0.0f to Color(0xFF667EEA),
        0.3f to Color(0xFF764BA2),
        0.6f to Color(0xFFF093FB),
        0.8f to Color(0xFF4FACFE),
        1.0f to Color(0xFF00F2FE)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dynamicGradient)
    ) {
        // Animated blur circles (liquid effect)
        Box(
            modifier = Modifier
                .offset(x = (gradientOffset * 0.1f).dp, y = (gradientOffset * 0.05f).dp)
                .size(300.dp)
                .background(Color(0x33FF6B9D), RoundedCornerShape(50))
                .blur(80.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-gradientOffset * 0.08f).dp, y = (gradientOffset * 0.1f).dp)
                .size(250.dp)
                .background(Color(0x33FEC163), RoundedCornerShape(50))
                .blur(90.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (gradientOffset * 0.06f).dp, y = (-gradientOffset * 0.07f).dp)
                .size(280.dp)
                .background(Color(0x3300D4FF), RoundedCornerShape(50))
                .blur(85.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero Section
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Glass logo container
                    Surface(
                        shape = RoundedCornerShape(32.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .size(120.dp)
                            .scale(scale)
                            .shadow(20.dp, RoundedCornerShape(32.dp), spotColor = Color.Black.copy(0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.25f),
                                            Color.White.copy(alpha = 0.05f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader_anim))
                            val progress by animateLottieCompositionAsState(
                                composition,
                                iterations = LottieConstants.IterateForever,
                                isPlaying = true
                            )
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier.fillMaxSize(0.7f)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        "DineSmart",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp,
                            letterSpacing = (-1).sp
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Discover extraordinary dining experiences",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(Modifier.height(28.dp))

                    // Glass navigation buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GlassButton(
                            text = "Explore",
                            icon = Icons.Rounded.Restaurant,
                            onClick = { navController.navigate(Routes.LIST) },
                            modifier = Modifier.weight(1f),
                            isPrimary = true
                        )
                        GlassButton(
                            text = "Map",
                            icon = Icons.Rounded.LocationOn,
                            onClick = { navController.navigate(Routes.MAP) },
                            modifier = Modifier.weight(1f)
                        )
                        GlassButton(
                            text = "About",
                            icon = Icons.Rounded.Info,
                            onClick = { navController.navigate(Routes.ABOUT) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Featured Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Featured Places",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            "${featured.size}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Featured restaurants
            items(featured) { restaurant ->
                GlassRestaurantCard(
                    restaurant = restaurant,
                    onClick = { navController.navigate(Routes.detailsRoute(restaurant.id)) }
                )
            }

            // Stats Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassStatCard("24", "Restaurants", Icons.Rounded.Restaurant, Modifier.weight(1f))
                    GlassStatCard("4.5", "Avg Rating", Icons.Rounded.Star, Modifier.weight(1f))
                    GlassStatCard("8", "Open Now", Icons.Rounded.Schedule, Modifier.weight(1f))
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun GlassButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (isPrimary) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.15f),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isPrimary) 0.35f else 0.2f),
                            Color.White.copy(alpha = if (isPrimary) 0.15f else 0.05f)
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
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun GlassRestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.12f),
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.18f),
                            Color.White.copy(alpha = 0.06f)
                        )
                    )
                )
        ) {
            Column {
                // Image with overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(restaurant.image)
                            .crossfade(400)
                            .build(),
                        contentDescription = restaurant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                        error = painterResource(android.R.drawable.ic_menu_gallery)
                    )

                    // Dark gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    ),
                                    startY = 50f
                                )
                            )
                    )

                    // Rating badge (top right)
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.25f),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "${restaurant.rating}.0",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    // Name and cuisine (bottom)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            restaurant.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                restaurant.tags,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Info section
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            restaurant.address,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 20.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.Phone,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            restaurant.phone,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassStatCard(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.15f),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
