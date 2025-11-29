package com.example.dinesmart.data

import android.content.Context
import com.example.dinesmart.data.firebase.FirebaseReviewService
import com.example.dinesmart.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReviewRepository(context: Context) {
    private val reviewDao = AppDatabase.getInstance(context).reviewDao()
    private val firebaseService = FirebaseReviewService()

    fun getReviewsForRestaurant(restaurantId: Int): Flow<List<Review>> {
        return reviewDao.getReviewsForRestaurant(restaurantId).map { entities ->
            entities.map { it.toReview() }
        }
    }

    fun getFirebaseReviews(restaurantId: Int): Flow<List<Review>> {
        return firebaseService.getReviewsForRestaurant(restaurantId)
    }

    suspend fun addReview(review: Review, userId: String, userName: String): Result<Review> {
        return try {
            val firebaseResult = firebaseService.addReview(review)

            if (firebaseResult.isSuccess) {
                val firebaseId = firebaseResult.getOrNull()
                val reviewWithFirebaseId = review.copy(
                    userId = userId,
                    userName = userName,
                    firebaseId = firebaseId
                )

                val localId = reviewDao.insert(reviewWithFirebaseId.toEntity())
                Result.success(reviewWithFirebaseId.copy(id = localId))
            } else {
                val localId = reviewDao.insert(review.toEntity())
                Result.success(review.copy(id = localId))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(review: Review): Result<Unit> {
        return try {
            review.firebaseId?.let {
                firebaseService.deleteReview(it)
            }

            reviewDao.delete(review.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAverageRating(restaurantId: Int): Float {
        return reviewDao.getAverageRating(restaurantId) ?: 0f
    }

    suspend fun getReviewCount(restaurantId: Int): Int {
        return reviewDao.getReviewCount(restaurantId)
    }

    suspend fun syncReviews(restaurantId: Int) {
        try {
            val firebaseRating = firebaseService.getAverageRating(restaurantId)
        } catch (e: Exception) {
        }
    }
}

