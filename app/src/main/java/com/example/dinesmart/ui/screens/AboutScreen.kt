package com.example.dinesmart.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    val context = LocalContext.current
    val versionName = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName ?: "1.0.0"
    } catch (_: Exception) {
        // Fallback if package info can't be retrieved
        "1.0.0"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .semantics { contentDescription = "About screen" },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with circular icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "App logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "DineSmart",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Version: $versionName", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "DineSmart helps you discover, save and navigate to your favorite restaurants. Browse curated lists, see realtime locations, and get contact details instantly.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Features list
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Key features:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                FeatureRow(icon = Icons.Default.Star, text = "Curated & rated restaurants")
                FeatureRow(icon = Icons.Default.OpenInBrowser, text = "Open maps & contact actions")
                FeatureRow(icon = Icons.Default.Share, text = "Share restaurants with friends")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Developer/contact card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Developer info card" },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Developer", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Prabesh Shrestha - 101538718", style = MaterialTheme.typography.bodyMedium)
                    Text("Moksh Chhetri - 101515045", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: Prabesh.Shrestha@georgebrown.ca", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = {
                            // Email intent
                            try {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:")
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("Prabesh.Shrestha@georgebrown.ca"))
                                    putExtra(Intent.EXTRA_SUBJECT, "Feedback for DineSmart")
                                }
                                context.startActivity(intent)
                            } catch (_: Exception) {
                            }
                        }, modifier = Modifier.semantics { contentDescription = "Send email" }) {
                            Icon(Icons.Default.Email, contentDescription = "Email icon")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Email")
                        }

                        TextButton(onClick = {
                            // Open website
                            try {
                                val web = "https://dinesmart.example".let { Uri.parse(it) }
                                val webIntent = Intent(Intent.ACTION_VIEW, web)
                                context.startActivity(webIntent)
                            } catch (_: Exception) {
                            }
                        }, modifier = Modifier.semantics { contentDescription = "Open website" }) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = "Website icon")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Website")
                        }

                        TextButton(onClick = {
                            // Share app
                            try {
                                val share = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Check out DineSmart")
                                    putExtra(Intent.EXTRA_TEXT, "Check out DineSmart - discover great restaurants: https://dinesmart.example")
                                }
                                context.startActivity(Intent.createChooser(share, "Share DineSmart"))
                            } catch (_: Exception) {
                            }
                        }, modifier = Modifier.semantics { contentDescription = "Share app" }) {
                            Icon(Icons.Default.Share, contentDescription = "Share icon")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rate & Privacy row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = {
                    // Open Play Store / web fallback
                    try {
                        val uri = Uri.parse("market://details?id=${context.packageName}")
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        if (goToMarket.resolveActivity(context.packageManager) != null) {
                            context.startActivity(goToMarket)
                        } else {
                            val webUri = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
                        }
                    } catch (_: Exception) {
                    }
                }, modifier = Modifier.semantics { contentDescription = "Rate app" }) {
                    Text("Rate")
                }

                TextButton(onClick = {
                    // Open privacy / terms (placeholder)
                    try {
                        val uri = Uri.parse("https://dinesmart.example/privacy")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } catch (_: Exception) {
                    }
                }, modifier = Modifier.semantics { contentDescription = "Open privacy policy" }) {
                    Text("Privacy")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Made with ❤ for food lovers", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun FeatureRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
