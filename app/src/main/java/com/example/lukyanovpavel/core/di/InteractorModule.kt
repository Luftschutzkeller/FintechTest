package com.example.lukyanovpavel.core.di

import com.example.lukyanovpavel.data.repository.PostsRepository
import com.example.lukyanovpavel.domain.common.ObjectStorage
import com.example.lukyanovpavel.domain.posts.LoadPostsInteractor
import com.example.lukyanovpavel.domain.posts.LoadPostsInteractorImpl
import com.example.lukyanovpavel.domain.posts.Post
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {

    @Singleton
    @Provides
    fun provideLoadPostsInteractor(
        repo: PostsRepository,
        storage: ObjectStorage<Post>
    ): LoadPostsInteractor = LoadPostsInteractorImpl(repo, storage)
}