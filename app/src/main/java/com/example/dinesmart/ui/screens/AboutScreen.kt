package com.example.dinesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val versionName = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName ?: "1.0.0"
    } catch (_: Exception) {
        // Fallback if package info can't be retrieved
        "1.0.0"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("About DineSmart", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "DineSmart is your go-to app for discovering the best restaurants around you. " +
                        "Whether you're looking for a quick bite or a fine dining experience, " +
                        "we've got you covered!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Version: $versionName", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
