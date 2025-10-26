package com.example.dinesmart.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dinesmart.navigation.Routes
import com.example.dinesmart.R
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.data.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()
    val featured: List<Restaurant> = if (restaurantsState.isNotEmpty()) restaurantsState.take(5) else listOf(
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
            animation = keyframes {
                durationMillis = 1400
                1.06f at 700 with FastOutSlowInEasing
            },
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
                        LottieAnimation(composition, progress, modifier = Modifier.fillMaxSize())
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
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Text("Map")
                    }
                }
            }

            // Middle content: featured restaurants (responsive widths)
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

                BoxWithConstraints {
                    val maxW: Dp = this.maxWidth
                    // cardWidth is 60% of available width but clamped between 160 and 320 dp
                    val cardWidth: Dp = (maxW * 0.6f).coerceIn(160.dp, 320.dp)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(featured) { item ->
                            Card(
                                modifier = Modifier
                                    .widthIn(min = 140.dp, max = cardWidth)
                                    .heightIn(min = 120.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0x99FFFFFF))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(6.dp))
                                    Text(item.tags, style = MaterialTheme.typography.bodySmall)
                                    Spacer(Modifier.height(10.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("${item.rating} \u2b50", style = MaterialTheme.typography.bodyMedium)
                                        Button(onClick = { navController.navigate(Routes.detailsRoute(item.id)) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B2DFF))) {
                                            Text("Open", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom stats bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(shape = RoundedCornerShape(12.dp), color = Color(0x66FFFFFF)) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("24", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
                        Column { Text("Restaurants", style = MaterialTheme.typography.bodySmall.copy(color = Color.White)) }
                    }
                }

                Surface(shape = RoundedCornerShape(12.dp), color = Color(0x66FFFFFF)) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("4.5", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
                        Column { Text("Avg Rating", style = MaterialTheme.typography.bodySmall.copy(color = Color.White)) }
                    }
                }

                Surface(shape = RoundedCornerShape(12.dp), color = Color(0x66FFFFFF)) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("8", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
                        Column { Text("Open Now", style = MaterialTheme.typography.bodySmall.copy(color = Color.White)) }
                    }
                }
            }
        }
    }
}

// (kept for compatibility; actual data now comes from RestaurantViewModel)
