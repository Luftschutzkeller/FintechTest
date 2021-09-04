package com.example.lukyanovpavel.domain.posts.latest

import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Completable
import io.reactivex.Single

interface LatestPostsRepository {
    fun load(page: Int): Completable

    fun getPost(
        page: Int,
        count: Int
    ): Single<List<Post>>
}