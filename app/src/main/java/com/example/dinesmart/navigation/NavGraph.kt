package com.example.dinesmart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dinesmart.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { com.example.dinesmart.ui.screens.SplashScreen(navController) }
        composable("list") { RestaurantListScreen(navController) }
        composable("details") { RestaurantDetailScreen(navController) }
        composable("add") { AddRestaurantScreen(navController) }
        composable("map") { MapScreen(navController) }
        composable("about") { AboutScreen() }
    }
}
