package com.example.dinesmart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.dinesmart.navigation.Routes
import com.example.dinesmart.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.LIST) { RestaurantListScreen(navController) }
        composable(Routes.DETAILS, arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            RestaurantDetailScreen(navController, id)
        }
        composable(Routes.ADD) { AddRestaurantScreen(navController) }
        composable(Routes.MAP) { MapScreen(navController) }
        composable(Routes.ABOUT) { AboutScreen(navController) }
    }
}
