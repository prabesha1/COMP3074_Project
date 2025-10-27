package com.example.dinesmart.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dinesmart.R
import com.example.dinesmart.data.Restaurant
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.navigation.Routes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val vm: RestaurantViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(context.applicationContext as android.app.Application))
    val restaurantsState by vm.restaurants.collectAsState()

    // Featured items (fallback sample if DB empty)
    val featured: List<Restaurant> = if (restaurantsState.isNotEmpty()) restaurantsState.take(6) else listOf(
        Restaurant(1, "Sushi Place", "Japanese • Sushi", 4),
        Restaurant(2, "Burger Hub", "Fast Food", 5),
        Restaurant(3, "Spice Garden", "Indian", 3)
    )

    // Logo pulse animation
    val transition = rememberInfiniteTransition()
    val scale by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF7C4DFF), Color(0xFF42A5F5))
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.background(gradient).fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader_anim))
                    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever, isPlaying = true)

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0x66FFFFFF),
                        modifier = Modifier
                            .size(88.dp)
                            .scale(scale)
                            .shadow(6.dp, RoundedCornerShape(18.dp))
                            .semantics { contentDescription = "App animation" }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            LottieAnimation(composition = composition, progress = { progress }, modifier = Modifier.fillMaxSize())
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Welcome to DineSmart",
                        style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text("Discover great food around you", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xDDFFFFFF)))

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { navController.navigate(Routes.LIST) }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                            Text("Explore", color = Color(0xFF3B2DFF))
                        }
                        OutlinedButton(onClick = { navController.navigate(Routes.MAP) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White), border = ButtonDefaults.outlinedButtonBorder(enabled = true)) {
                            Text("Map")
                        }
                        OutlinedButton(onClick = { navController.navigate(Routes.ABOUT) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White), border = ButtonDefaults.outlinedButtonBorder(enabled = true), modifier = Modifier.semantics { contentDescription = "Open About" }) {
                            Text("About")
                        }
                    }
                }

                // Featured: takes available space
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)) {

                    Text("Featured", style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.SemiBold))
                    Spacer(Modifier.height(8.dp))

                    Surface(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(12.dp), color = Color(0x33FFFFFF)) {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(featured) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate(Routes.detailsRoute(item.id)) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                                            contentDescription = "${item.name} thumbnail",
                                            modifier = Modifier
                                                .size(width = 80.dp, height = 56.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )

                                        Spacer(Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(item.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                            Spacer(Modifier.height(4.dp))
                                            Text(item.tags, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                            Spacer(Modifier.height(6.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                                                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                                                        Spacer(Modifier.width(6.dp))
                                                        Text("${item.rating}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                                    }
                                                }
                                                Spacer(Modifier.width(8.dp))
                                                Text(if (item.rating >= 4) "Highly rated" else "Good choice", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Footer stats
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatPill("24", "Restaurants")
                    StatPill("4.5", "Avg Rating")
                    StatPill("8", "Open Now")
                }
            }
        }
    }
}

@Composable
private fun StatPill(value: String, label: String) {
    Surface(shape = RoundedCornerShape(16.dp), color = Color(0x66FFFFFF)) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
        }
    }
}
