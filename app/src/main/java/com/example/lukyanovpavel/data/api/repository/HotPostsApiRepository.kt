package com.example.lukyanovpavel.data.api.repository

import com.example.lukyanovpavel.core.networkmonitor.NetworkMonitor
import com.example.lukyanovpavel.data.api.dto.PostDto
import com.example.lukyanovpavel.data.api.services.PostsService
import com.example.lukyanovpavel.utils.error.handleError
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

interface HotPostsApiRepository : (Int) -> Single<List<PostDto>>

class HotPostsApiRepositoryImpl @Inject constructor(
    private val api: PostsService,
    private val networkMonitor: NetworkMonitor,
) : HotPostsApiRepository {
    override fun invoke(page: Int): Single<List<PostDto>> =
        api.getHot(page)
            .handleError(networkMonitor.isNetworkAvailable())
            .map { posts ->
                posts.result
            }
            .doOnError(Timber::e)
}