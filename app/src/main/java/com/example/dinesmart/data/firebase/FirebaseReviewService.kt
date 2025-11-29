package com.example.dinesmart.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.dinesmart.data.Review
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseReviewService {
    private val firestore = FirebaseFirestore.getInstance()
    private val reviewsCollection = firestore.collection("reviews")

    // Add a review to Firebase
    suspend fun addReview(review: Review): Result<String> {
        return try {
            val reviewMap = hashMapOf(
                "restaurantId" to review.restaurantId,
                "userId" to review.userId,
                "userName" to review.userName,
                "rating" to review.rating,
                "comment" to review.comment,
                "timestamp" to review.timestamp
            )

            val docRef = reviewsCollection.add(reviewMap).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get reviews for a restaurant as a Flow
    fun getReviewsForRestaurant(restaurantId: Int): Flow<List<Review>> = callbackFlow {
        val listener = reviewsCollection
            .whereEqualTo("restaurantId", restaurantId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reviews = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Review(
                            firebaseId = doc.id,
                            restaurantId = doc.getLong("restaurantId")?.toInt() ?: 0,
                            userId = doc.getString("userId") ?: "",
                            userName = doc.getString("userName") ?: "Anonymous",
                            rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                            comment = doc.getString("comment") ?: "",
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(reviews)
            }

        awaitClose { listener.remove() }
    }

    // Delete a review from Firebase
    suspend fun deleteReview(firebaseId: String): Result<Unit> {
        return try {
            reviewsCollection.document(firebaseId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAverageRating(restaurantId: Int): Float {
        return try {
            val snapshot = reviewsCollection
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await()

            val ratings = snapshot.documents.mapNotNull {
                it.getDouble("rating")?.toFloat()
            }

            if (ratings.isEmpty()) 0f else ratings.average().toFloat()
        } catch (e: Exception) {
            0f
        }
    }
}

