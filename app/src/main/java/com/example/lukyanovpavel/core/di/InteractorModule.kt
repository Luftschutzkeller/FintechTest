package com.example.lukyanovpavel.core.di

import com.example.lukyanovpavel.domain.posts.hot.HotPostsInteractor
import com.example.lukyanovpavel.domain.posts.hot.HotPostsInteractorImpl
import com.example.lukyanovpavel.domain.posts.hot.HotPostsRepository
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsInteractor
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsInteractorImpl
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsRepository
import com.example.lukyanovpavel.domain.posts.top.TopPostsInteractor
import com.example.lukyanovpavel.domain.posts.top.TopPostsInteractorImpl
import com.example.lukyanovpavel.domain.posts.top.TopPostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {

    @Provides
    fun provideTopPostsInteractor(
        repo: TopPostsRepository
    ): TopPostsInteractor = TopPostsInteractorImpl(repo)

    @Provides
    fun provideHotPostsInteractor(
        repo: HotPostsRepository
    ): HotPostsInteractor = HotPostsInteractorImpl(repo)

    @Provides
    fun provideLatestPostsInteractor(
        repo: LatestPostsRepository
    ): LatestPostsInteractor = LatestPostsInteractorImpl(repo)
}