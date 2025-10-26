package com.example.dinesmart.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY id")
    suspend fun getAll(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RestaurantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RestaurantEntity>)
}

