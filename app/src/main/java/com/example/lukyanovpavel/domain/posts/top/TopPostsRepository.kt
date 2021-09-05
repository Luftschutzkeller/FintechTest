package com.example.lukyanovpavel.domain.posts.top

import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Completable
import io.reactivex.Single

interface TopPostsRepository {
    fun getPost(
        page: Int,
        count: Int
    ): Single<Post>
}