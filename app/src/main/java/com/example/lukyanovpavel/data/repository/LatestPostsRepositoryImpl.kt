package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.dto.PostDto
import com.example.lukyanovpavel.data.api.dto.toDomain
import com.example.lukyanovpavel.data.api.repository.LatestPostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LatestPostsRepositoryImpl @Inject constructor(
    private val latestApi: LatestPostsApiRepository,
    private val db: PostsDatabaseRepository
) : LatestPostsRepository {

    private fun load(page: Int, count: Int): Single<PostDto> =
        latestApi.invoke(page)
            .map { posts ->
                db.insertPost(LATEST, posts, page)
                    .subscribe()
                posts[count]
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(page: Int, count: Int): Single<Post> =
        db.getPost(LATEST, page, count)
            .flatMap { listPostsEntity ->
                if (listPostsEntity.isEmpty()) {
                    load(page, count)
                        .map { it.toDomain() }
                } else {
                    Single.just(listPostsEntity.first().toDomain())
                }
            }

    companion object {
        const val LATEST = "latest"
    }
}