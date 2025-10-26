package com.example.dinesmart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dinesmart.navigation.Routes
import com.example.dinesmart.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.LIST) { RestaurantListScreen(navController) }
        composable(Routes.DETAILS) { RestaurantDetailScreen(navController) }
        composable(Routes.ADD) { AddRestaurantScreen(navController) }
        composable(Routes.MAP) { MapScreen(navController) }
        composable(Routes.ABOUT) { AboutScreen(navController) }
    }
}
