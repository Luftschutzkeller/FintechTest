package com.example.lukyanovpavel.core.di

import com.example.lukyanovpavel.core.networkmonitor.NetworkMonitor
import com.example.lukyanovpavel.data.api.repository.PostsApiRepository
import com.example.lukyanovpavel.data.api.repository.PostsApiRepositoryImpl
import com.example.lukyanovpavel.data.api.services.PostsService
import com.example.lukyanovpavel.data.database.DevLifeDatabase
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepositoryImpl
import com.example.lukyanovpavel.data.repository.PostsRepository
import com.example.lukyanovpavel.data.repository.PostsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun providePostsApiRepository(
        api: PostsService,
        networkMonitor: NetworkMonitor
    ): PostsApiRepository = PostsApiRepositoryImpl(api, networkMonitor)

    @Singleton
    @Provides
    fun providePostsRepository(
        api: PostsApiRepository,
        db: PostsDatabaseRepository
    ): PostsRepository = PostsRepositoryImpl(api, db)

    @Singleton
    @Provides
    fun providePostsDatabaseRepository(
        db: DevLifeDatabase
    ): PostsDatabaseRepository = PostsDatabaseRepositoryImpl(db)
}