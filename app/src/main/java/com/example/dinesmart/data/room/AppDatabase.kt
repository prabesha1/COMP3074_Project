package com.example.dinesmart.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class, ReviewEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        private const val DB_NAME = "dinesmart.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration(true)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
