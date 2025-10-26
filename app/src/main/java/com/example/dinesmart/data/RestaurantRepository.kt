package com.example.dinesmart.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONArray
import com.example.dinesmart.data.room.AppDatabase
import com.example.dinesmart.data.room.RestaurantEntity
import kotlinx.coroutines.withContext

// Repository backed by Room. On first read, it will seed the DB from assets if empty.
class RestaurantRepository(private val context: Context) {

    private val db by lazy { AppDatabase.getInstance(context) }

    fun getRestaurants(): Flow<List<Restaurant>> = flow {
        // Ensure DB seeded
        withContext(Dispatchers.IO) {
            val dao = db.restaurantDao()
            val existing = dao.getAll()
            if (existing.isEmpty()) {
                // attempt to seed from assets
                val json = try { context.assets.open("restaurants.json").bufferedReader().use { it.readText() } } catch (e: Exception) { null }
                if (json != null) {
                    try {
                        val arr = JSONArray(json)
                        val list = mutableListOf<RestaurantEntity>()
                        for (i in 0 until arr.length()) {
                            val obj = arr.getJSONObject(i)
                            val entity = RestaurantEntity(
                                id = obj.optInt("id", i),
                                name = obj.optString("name", "Unnamed"),
                                tags = obj.optString("tags", ""),
                                rating = obj.optInt("rating", 0),
                                address = obj.optString("address", ""),
                                phone = obj.optString("phone", ""),
                                lat = if (obj.has("lat")) obj.optDouble("lat") else null,
                                lng = if (obj.has("lng")) obj.optDouble("lng") else null
                            )
                            list.add(entity)
                        }
                        if (list.isNotEmpty()) dao.insertAll(list)
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        }

        // Emit DB contents
        try {
            val dao = db.restaurantDao()
            val entities = dao.getAll()
            val mapped = entities.map { e -> Restaurant(e.id, e.name, e.tags, e.rating, e.address, e.phone, e.lat, e.lng) }
            emit(mapped)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getById(id: Int): Restaurant? = withContext(Dispatchers.IO) {
        val entity = db.restaurantDao().getById(id)
        entity?.let { Restaurant(it.id, it.name, it.tags, it.rating, it.address, it.phone, it.lat, it.lng) }
    }

}
