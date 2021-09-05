package com.example.lukyanovpavel.core.di

import com.example.lukyanovpavel.core.networkmonitor.NetworkMonitor
import com.example.lukyanovpavel.data.api.repository.*
import com.example.lukyanovpavel.data.api.services.PostsService
import com.example.lukyanovpavel.data.database.DevLifeDatabase
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepositoryImpl
import com.example.lukyanovpavel.data.repository.HotPostsRepositoryImpl
import com.example.lukyanovpavel.data.repository.LatestPostsRepositoryImpl
import com.example.lukyanovpavel.data.repository.TopPostsRepositoryImpl
import com.example.lukyanovpavel.domain.posts.hot.HotPostsRepository
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsRepository
import com.example.lukyanovpavel.domain.posts.top.TopPostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    // Api
    @Singleton
    @Provides
    fun provideTopPostsApiRepository(
        api: PostsService,
        networkMonitor: NetworkMonitor
    ): TopPostsApiRepository = TopPostsApiRepositoryImpl(api, networkMonitor)

    @Singleton
    @Provides
    fun provideHotPostsApiRepository(
        api: PostsService,
        networkMonitor: NetworkMonitor
    ): HotPostsApiRepository = HotPostsApiRepositoryImpl(api, networkMonitor)

    @Singleton
    @Provides
    fun provideLatestPostsApiRepository(
        api: PostsService,
        networkMonitor: NetworkMonitor
    ): LatestPostsApiRepository = LatestPostsApiRepositoryImpl(api, networkMonitor)

    // Repository
    @Singleton
    @Provides
    fun provideTopPostsRepository(
        topApi: TopPostsApiRepository,
        db: PostsDatabaseRepository
    ): TopPostsRepository =
        TopPostsRepositoryImpl(topApi, db)

    @Singleton
    @Provides
    fun provideHotPostsRepository(
        hotApi: HotPostsApiRepository,
        db: PostsDatabaseRepository
    ): HotPostsRepository =
        HotPostsRepositoryImpl(hotApi, db)

    @Singleton
    @Provides
    fun provideLatestPostsRepository(
        latestApi: LatestPostsApiRepository,
        db: PostsDatabaseRepository
    ): LatestPostsRepository =
        LatestPostsRepositoryImpl(latestApi, db)

    // Database
    @Singleton
    @Provides
    fun providePostsDatabaseRepository(
        db: DevLifeDatabase
    ): PostsDatabaseRepository = PostsDatabaseRepositoryImpl(db)
}