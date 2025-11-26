package com.example.dinesmart.data

data class Review(
    val id: Long = 0,
    val restaurantId: Int,
    val userId: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis(),
    val firebaseId: String? = null
)

// Extension functions to convert between entities
fun Review.toEntity() = com.example.dinesmart.data.room.ReviewEntity(
    id = id,
    restaurantId = restaurantId,
    userId = userId,
    userName = userName,
    rating = rating,
    comment = comment,
    timestamp = timestamp,
    firebaseId = firebaseId
)

fun com.example.dinesmart.data.room.ReviewEntity.toReview() = Review(
    id = id,
    restaurantId = restaurantId,
    userId = userId,
    userName = userName,
    rating = rating,
    comment = comment,
    timestamp = timestamp,
    firebaseId = firebaseId
)

