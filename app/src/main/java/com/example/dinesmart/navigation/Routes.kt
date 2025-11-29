package com.example.dinesmart.navigation

object Routes {
    const val SPLASH = "splash"
    const val LIST = "list"
    const val DETAILS = "details/{id}"
    const val ADD = "add"
    const val MAP = "map"
    const val ABOUT = "about"

    fun detailsRoute(id: Int) = "details/$id"
}
