package com.example.dinesmart.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["restaurantId"])]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val restaurantId: Int,
    val userId: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis(),
    val firebaseId: String? = null // For syncing with Firebase
)
