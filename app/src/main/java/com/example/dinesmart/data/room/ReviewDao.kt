package com.example.dinesmart.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE restaurantId = :restaurantId ORDER BY timestamp DESC")
    fun getReviewsForRestaurant(restaurantId: Int): Flow<List<ReviewEntity>>

    @Query("SELECT AVG(rating) FROM reviews WHERE restaurantId = :restaurantId")
    suspend fun getAverageRating(restaurantId: Int): Float?

    @Query("SELECT COUNT(*) FROM reviews WHERE restaurantId = :restaurantId")
    suspend fun getReviewCount(restaurantId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: ReviewEntity): Long

    @Delete
    suspend fun delete(review: ReviewEntity)

    @Query("DELETE FROM reviews WHERE restaurantId = :restaurantId")
    suspend fun deleteReviewsForRestaurant(restaurantId: Int)

    @Query("SELECT * FROM reviews WHERE firebaseId = :firebaseId")
    suspend fun getReviewByFirebaseId(firebaseId: String): ReviewEntity?
}

