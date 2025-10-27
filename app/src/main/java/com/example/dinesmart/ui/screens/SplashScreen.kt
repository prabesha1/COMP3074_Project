package com.example.dinesmart.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dinesmart.navigation.Routes
import com.example.dinesmart.R
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.data.Restaurant
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()
    val featured: List<Restaurant> = if (restaurantsState.isNotEmpty()) restaurantsState.take(6) else listOf(
        Restaurant(1, "Sushi Place", "Japanese \u2022 Sushi", 4),
        Restaurant(2, "Burger Hub", "Fast Food", 5),
        Restaurant(3, "Spice Garden", "Indian", 3)
    )

    // Animation for the logo pulse
    val transition = rememberInfiniteTransition()
    val scale by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Background gradient
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF7C4DFF), Color(0xFF42A5F5))
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .background(brush = gradient)
                .fillMaxSize()
        ) {
            // Top floating card with animated logo and CTA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated logo (Lottie)
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader_anim))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    isPlaying = true,
                )

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0x66FFFFFF),
                    modifier = Modifier
                        .sizeIn(minWidth = 80.dp, minHeight = 80.dp, maxWidth = 160.dp, maxHeight = 160.dp)
                        .scale(scale)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .semantics { contentDescription = "App animation" }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // pass progress as a lambda to the LottieAnimation (newer API)
                        LottieAnimation(composition, { progress }, modifier = Modifier.fillMaxSize())
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Welcome to DineSmart",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Discover great food around you",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xDDFFFFFF))
                )

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { navController.navigate(Routes.LIST) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Explore", color = Color(0xFF3B2DFF))
                    }

                    OutlinedButton(
                        onClick = { navController.navigate(Routes.MAP) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Text("Map")
                    }
                }
            }

            // Middle content: featured restaurants (vertical list with refined design)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    "Featured",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(8.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .padding(bottom = 100.dp), // leave room for bottom stats
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x33FFFFFF)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(featured) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                , elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    // circular avatar with initials
                                    val initials = item.name.split(' ').mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(56.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(initials, style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer))
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(Modifier.weight(1f)) {
                                        Text(item.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Spacer(Modifier.height(4.dp))
                                        Text(item.tags, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Spacer(Modifier.height(8.dp))
                                        // rating row
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                                                Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                                                    Spacer(Modifier.width(6.dp))
                                                    Text("${item.rating}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                                }
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            Text("Highly rated", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    // compact Open button
                                    Column(horizontalAlignment = Alignment.End) {
                                        OutlinedButton(
                                            onClick = { navController.navigate(Routes.detailsRoute(item.id)) },
                                            border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                        ) {
                                            Text("Open")
                                        }
                                        Spacer(Modifier.height(6.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom stats bar redesigned: pill-style stats for better visual balance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stat pill composable
                fun StatPill(value: String, label: String) = @Composable {
                    Surface(shape = RoundedCornerShape(16.dp), color = Color(0x66FFFFFF)) {
                        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(value, style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
                            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                        }
                    }
                }

                StatPill("24", "Restaurants")
                StatPill("4.5", "Avg Rating")
                StatPill("8", "Open Now")
            }
        }
    }
}

// (kept for compatibility; actual data now comes from RestaurantViewModel)
