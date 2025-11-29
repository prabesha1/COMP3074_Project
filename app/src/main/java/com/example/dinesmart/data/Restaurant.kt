package com.example.dinesmart.data

data class Restaurant(
    val id: Int,
    val name: String,
    val tags: String,
    val rating: Int,
    val address: String = "",
    val phone: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val image: String? = null
)
