package com.example.dinesmart.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val tags: String,
    val rating: Int,
    val address: String,
    val phone: String,
    val lat: Double?,
    val lng: Double?
)

