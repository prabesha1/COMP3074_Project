package com.example.dinesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dinesmart.navigation.Routes

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🍽️ DineSmart", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(20.dp))
            Button(onClick = { navController.navigate(Routes.LIST) }) {
                Text("Explore Restaurants")
            }
        }
    }
}
