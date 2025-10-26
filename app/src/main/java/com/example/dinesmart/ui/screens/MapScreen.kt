package com.example.dinesmart.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dinesmart.navigation.Routes
import com.example.dinesmart.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Google Map (placeholder)", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_map),
                contentDescription = "Map placeholder",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Tap on a marker to get directions.", style = MaterialTheme.typography.bodySmall)
        }
    }
}
