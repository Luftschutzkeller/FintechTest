package com.example.lukyanovpavel.domain.posts.latest

import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Completable
import io.reactivex.Single

interface LatestPostsRepository {
    fun getPost(
        page: Int,
        count: Int
    ): Single<Post>
}