package com.example.dinesmart.data

// Simple restaurant model used across the app
data class Restaurant(
    val id: Int,
    val name: String,
    val tags: String,
    val rating: Int,
    val address: String = "",
    val phone: String = "",
    val lat: Double? = null,
    val lng: Double? = null
)
