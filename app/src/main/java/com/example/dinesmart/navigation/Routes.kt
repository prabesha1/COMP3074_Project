package com.example.dinesmart.navigation

// Centralized navigation route names to avoid magic strings and typos
object Routes {
    const val SPLASH = "splash"
    const val LIST = "list"
    // Route pattern with an argument (id)
    const val DETAILS = "details/{id}"
    const val ADD = "add"
    const val MAP = "map"
    const val ABOUT = "about"

    fun detailsRoute(id: Int) = "details/$id"
}
