package com.example.lukyanovpavel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lukyanovpavel.data.database.dao.PostsDao
import com.example.lukyanovpavel.data.database.entity.PostEntity

@Database(
    entities = [
        PostEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DevLifeDatabase : RoomDatabase() {
    abstract fun posts(): PostsDao
}