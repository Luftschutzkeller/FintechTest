package com.example.lukyanovpavel.core.di

import android.content.Context
import androidx.room.Room
import com.example.lukyanovpavel.data.database.DevLifeDatabase
import com.example.lukyanovpavel.data.database.dao.PostsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DevLifeDatabase {
        return Room.databaseBuilder(
            context,
            DevLifeDatabase::class.java,
            "DevLifeDatabase.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    // Provides Dao
    @Provides
    fun providePosts(database: DevLifeDatabase): PostsDao =
        database.posts()
}