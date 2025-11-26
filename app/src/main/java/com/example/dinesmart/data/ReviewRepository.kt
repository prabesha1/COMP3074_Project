package com.example.dinesmart.data

import android.content.Context
import com.example.dinesmart.data.firebase.FirebaseReviewService
import com.example.dinesmart.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReviewRepository(context: Context) {
    private val reviewDao = AppDatabase.getInstance(context).reviewDao()
    private val firebaseService = FirebaseReviewService()

    // Get reviews from local database
    fun getReviewsForRestaurant(restaurantId: Int): Flow<List<Review>> {
        return reviewDao.getReviewsForRestaurant(restaurantId).map { entities ->
            entities.map { it.toReview() }
        }
    }

    // Get reviews from Firebase (real-time)
    fun getFirebaseReviews(restaurantId: Int): Flow<List<Review>> {
        return firebaseService.getReviewsForRestaurant(restaurantId)
    }

    // Add a review (both locally and to Firebase)
    suspend fun addReview(review: Review, userId: String, userName: String): Result<Review> {
        return try {
            // Add to Firebase first
            val firebaseResult = firebaseService.addReview(review)

            if (firebaseResult.isSuccess) {
                val firebaseId = firebaseResult.getOrNull()
                val reviewWithFirebaseId = review.copy(
                    userId = userId,
                    userName = userName,
                    firebaseId = firebaseId
                )

                // Then add to local database
                val localId = reviewDao.insert(reviewWithFirebaseId.toEntity())
                Result.success(reviewWithFirebaseId.copy(id = localId))
            } else {
                // If Firebase fails, still save locally
                val localId = reviewDao.insert(review.toEntity())
                Result.success(review.copy(id = localId))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete a review
    suspend fun deleteReview(review: Review): Result<Unit> {
        return try {
            // Delete from Firebase if it has a Firebase ID
            review.firebaseId?.let {
                firebaseService.deleteReview(it)
            }

            // Delete from local database
            reviewDao.delete(review.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get average rating
    suspend fun getAverageRating(restaurantId: Int): Float {
        return reviewDao.getAverageRating(restaurantId) ?: 0f
    }

    // Get review count
    suspend fun getReviewCount(restaurantId: Int): Int {
        return reviewDao.getReviewCount(restaurantId)
    }

    // Sync Firebase reviews to local database
    suspend fun syncReviews(restaurantId: Int) {
        try {
            val firebaseRating = firebaseService.getAverageRating(restaurantId)
            // You can update the restaurant's rating here if needed
        } catch (e: Exception) {
            // Handle sync error
        }
    }
}

