package com.example.lukyanovpavel.domain.posts.hot

import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Single

interface HotPostsRepository {
    fun getPost(
        page: Int,
        count: Int
    ): Single<Post>
}