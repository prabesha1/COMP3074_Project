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

class RestaurantRepository(private val context: Context) {

    private val db by lazy { AppDatabase.getInstance(context) }

    fun getRestaurants(): Flow<List<Restaurant>> = flow {
        // Load initial data if empty
        withContext(Dispatchers.IO) {
            val dao = db.restaurantDao()
            val existing = dao.getAll()
            if (existing.isEmpty()) {
                val json = try { context.assets.open("restaurants.json").bufferedReader().use { it.readText() } } catch (e: Exception) { null }
                if (json != null) {
                    try {
                        val arr = JSONArray(json)
                        val list = mutableListOf<RestaurantEntity>()
                        for (i in 0 until arr.length()) {
                            val obj = arr.getJSONObject(i)
                            val image = obj.optString("image").takeIf { it.isNotBlank() }
                            val entity = RestaurantEntity(
                                id = obj.optInt("id", i),
                                name = obj.optString("name", "Unnamed"),
                                tags = obj.optString("tags", ""),
                                rating = obj.optInt("rating", 0),
                                address = obj.optString("address", ""),
                                phone = obj.optString("phone", ""),
                                lat = obj.optDouble("lat", Double.NaN).takeIf { !it.isNaN() },
                                lng = obj.optDouble("lng", Double.NaN).takeIf { !it.isNaN() },
                                image = image
                            )
                            list.add(entity)
                        }
                        if (list.isNotEmpty()) dao.insertAll(list)
                    } catch (e: Exception) {
                        android.util.Log.e("RestaurantRepository", "Error loading initial data", e)
                    }
                }
            }
        }

        // Now emit from the Flow that observes database changes
        db.restaurantDao().getAllFlow().collect { entities ->
            val mapped = entities.map { e ->
                Restaurant(
                    e.id,
                    e.name,
                    e.tags,
                    e.rating,
                    e.address,
                    e.phone,
                    e.lat,
                    e.lng,
                    e.image
                )
            }
            emit(mapped)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getById(id: Int): Restaurant? = withContext(Dispatchers.IO) {
        val entity = db.restaurantDao().getById(id)
        entity?.let { Restaurant(it.id, it.name, it.tags, it.rating, it.address, it.phone, it.lat, it.lng, it.image) }
    }

    suspend fun insert(restaurant: Restaurant) = withContext(Dispatchers.IO) {
        val entity = RestaurantEntity(
            id = restaurant.id,
            name = restaurant.name,
            tags = restaurant.tags,
            rating = restaurant.rating,
            address = restaurant.address,
            phone = restaurant.phone,
            lat = restaurant.lat,
            lng = restaurant.lng,
            image = restaurant.image
        )
        db.restaurantDao().insert(entity)
    }

    suspend fun loadSampleRestaurants() = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            val list = mutableListOf<RestaurantEntity>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val image = obj.optString("image").takeIf { it.isNotBlank() }
                val entity = RestaurantEntity(
                    id = obj.optInt("id", i),
                    name = obj.optString("name", "Unnamed"),
                    tags = obj.optString("tags", ""),
                    rating = obj.optInt("rating", 0),
                    address = obj.optString("address", ""),
                    phone = obj.optString("phone", ""),
                    lat = obj.optDouble("lat", Double.NaN).takeIf { !it.isNaN() },
                    lng = obj.optDouble("lng", Double.NaN).takeIf { !it.isNaN() },
                    image = image
                )
                list.add(entity)
            }
            if (list.isNotEmpty()) {
                db.restaurantDao().insertAll(list)
                android.util.Log.d("RestaurantRepository", "Successfully loaded ${list.size} sample restaurants")
            }
        } catch (e: Exception) {
            android.util.Log.e("RestaurantRepository", "Error loading sample restaurants", e)
        }
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        db.restaurantDao().deleteAll()
        android.util.Log.d("RestaurantRepository", "Deleted all restaurants")
    }

}
