package com.example.lukyanovpavel.core.di

import com.example.lukyanovpavel.domain.common.ObjectStorage
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.PostsStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun providePostsStorage(): ObjectStorage<Post> = PostsStorage
}