package com.example.dinesmart.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dinesmart.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Map View") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Google Map (placeholder)", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Image(painter = painterResource(id = android.R.drawable.ic_dialog_map), contentDescription = "Map")
        }
    }
}
