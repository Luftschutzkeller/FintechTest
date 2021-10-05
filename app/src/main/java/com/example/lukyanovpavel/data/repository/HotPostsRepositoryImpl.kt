package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.dto.PostDto
import com.example.lukyanovpavel.data.api.dto.toDomain
import com.example.lukyanovpavel.data.api.repository.HotPostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.hot.HotPostsRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HotPostsRepositoryImpl @Inject constructor(
    private val hotApi: HotPostsApiRepository,
    private val db: PostsDatabaseRepository
) : HotPostsRepository {

    private fun load(page: Int, count: Int): Single<PostDto> =
        hotApi.invoke(page)
            .map { posts ->
                db.insertPost(HOT, posts, page)
                    .subscribe()
                posts[count]
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(page: Int, count: Int): Single<Post> =
        db.getPost(HOT, page, count)
            .flatMap { listPostsEntity ->
                if (listPostsEntity.isEmpty()) {
                    load(page, count)
                        .map { it.toDomain() }
                } else {
                    Single.just(listPostsEntity.first().toDomain())
                }
            }

    companion object {
        const val HOT = "hot"
    }
}