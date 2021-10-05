package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.dto.toDomain
import com.example.lukyanovpavel.data.api.repository.TopPostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.top.TopPostsRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TopPostsRepositoryImpl @Inject constructor(
    private val topApi: TopPostsApiRepository,
    private val db: PostsDatabaseRepository
) : TopPostsRepository {

    private fun load(page: Int, count: Int): Single<Post> =
        topApi.invoke(page)
            .map { posts ->
                db.insertPost(TOP, posts, page)
                    .subscribe()
                posts[count].toDomain()
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(page: Int, count: Int): Single<Post> =
        db.getPost(TOP, page, count)
            .flatMap { listPostsEntity ->
                if (listPostsEntity.isEmpty()) {
                    load(page, count)
                } else {
                    Single.just(listPostsEntity.first().toDomain())
                }
            }

    companion object {
        const val TOP = "top"
    }
}