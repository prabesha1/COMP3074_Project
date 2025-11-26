package com.example.dinesmart.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dinesmart.data.Restaurant
import com.example.dinesmart.data.RestaurantViewModel
import com.example.dinesmart.ui.components.*
import com.example.dinesmart.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestaurantScreen(navController: NavHostController) {
    val vm: RestaurantViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(
            LocalContext.current.applicationContext as android.app.Application
        )
    )

    var name by remember { mutableStateOf("") }
    var cuisine by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3) }
    var showError by remember { mutableStateOf(false) }

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
            initialOffsetY = 80.dp,
            modifier = Modifier.align(Alignment.TopStart)
        )
        FloatingGradientOrb(
            color = LiquidMint,
            size = 240.dp,
            initialOffsetX = (-gradientOffset.dp * 0.08f),
            initialOffsetY = 250.dp,
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
                                    "Add Restaurant",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            },
                            navigationIcon = {
                                if (navController.previousBackStackEntry != null) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Icon(
                    Icons.Rounded.RestaurantMenu,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    "Add New Restaurant",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (showError) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 16.dp,
                        glassAlpha = 0.25f
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.ErrorOutline,
                                contentDescription = null,
                                tint = Color(0xFFFF3B30).copy(alpha = 0.9f),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "Please fill in all required fields",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                // Form Fields with Glass Effect
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp,
                    glassAlpha = 0.18f
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GlassTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Restaurant Name *",
                            isError = showError && name.isBlank(),
                            leadingIcon = Icons.Rounded.Restaurant,
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassTextField(
                            value = cuisine,
                            onValueChange = { cuisine = it },
                            label = "Cuisine Type *",
                            placeholder = "e.g., Italian, Japanese",
                            isError = showError && cuisine.isBlank(),
                            leadingIcon = Icons.Rounded.Fastfood,
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = "Address *",
                            isError = showError && address.isBlank(),
                            leadingIcon = Icons.Rounded.LocationOn,
                            singleLine = false,
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Phone Number *",
                            placeholder = "+1-XXX-XXX-XXXX",
                            isError = showError && phone.isBlank(),
                            leadingIcon = Icons.Rounded.Phone,
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassTextField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            label = "Image URL (Optional)",
                            placeholder = "https://...",
                            leadingIcon = Icons.Rounded.Image,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Rating Selector
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp,
                    glassAlpha = 0.18f
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Rating",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                "$rating / 5",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            (1..5).forEach { star ->
                                Surface(
                                    onClick = { rating = star },
                                    modifier = Modifier.size(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (star <= rating)
                                        Color.White.copy(alpha = 0.35f)
                                    else
                                        Color.White.copy(alpha = 0.12f),
                                    shadowElevation = if (star <= rating) 8.dp else 0.dp
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                if (star <= rating) {
                                                    Brush.radialGradient(
                                                        colors = listOf(
                                                            Color.White.copy(alpha = 0.4f),
                                                            Color.White.copy(alpha = 0.2f)
                                                        )
                                                    )
                                                } else {
                                                    Brush.radialGradient(
                                                        colors = listOf(
                                                            Color.White.copy(alpha = 0.15f),
                                                            Color.White.copy(alpha = 0.05f)
                                                        )
                                                    )
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = "$star stars",
                                            tint = if (star <= rating)
                                                AccentYellow
                                            else
                                                Color.White.copy(alpha = 0.4f),
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassButton(
                        text = "Cancel",
                        onClick = { navController.popBackStack() },
                        icon = Icons.Rounded.Close,
                        modifier = Modifier.weight(1f)
                    )

                    GlassButton(
                        text = "Save",
                        onClick = {
                            if (name.isBlank() || cuisine.isBlank() || address.isBlank() || phone.isBlank()) {
                                showError = true
                            } else {
                                val newRestaurant = Restaurant(
                                    id = System.currentTimeMillis().toInt(),
                                    name = name,
                                    tags = cuisine,
                                    rating = rating,
                                    address = address,
                                    phone = phone,
                                    image = imageUrl.ifBlank { null }
                                )
                                vm.addRestaurant(newRestaurant)
                                navController.popBackStack()
                            }
                        },
                        icon = Icons.Rounded.Check,
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
