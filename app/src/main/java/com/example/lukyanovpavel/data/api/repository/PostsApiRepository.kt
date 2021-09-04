package com.example.lukyanovpavel.data.api.repository

import com.example.lukyanovpavel.core.networkmonitor.NetworkMonitor
import com.example.lukyanovpavel.data.api.dto.PostDto
import com.example.lukyanovpavel.data.api.services.PostsService
import com.example.lukyanovpavel.utils.error.handleError
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

interface PostsApiRepository : (String, Int) -> Single<List<PostDto>>

class PostsApiRepositoryImpl @Inject constructor(
    private val api: PostsService,
    private val networkMonitor: NetworkMonitor,
) : PostsApiRepository {
    override fun invoke(category: String, page: Int): Single<List<PostDto>> =
        api.getPosts(category, page)
            .handleError(networkMonitor.isNetworkAvailable())
            .map { posts ->
                posts.result
            }
            .doOnError(Timber::e)
}