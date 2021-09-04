package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.repository.TopPostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.top.TopPostsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TopPostsRepositoryImpl @Inject constructor(
    private val topApi: TopPostsApiRepository,
    private val db: PostsDatabaseRepository
) : TopPostsRepository {

    override fun load(page: Int): Completable =
        topApi.invoke(page)
            .flatMapCompletable { posts ->
                db.insertPost(TOP, posts, page)
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(page: Int, count: Int): Single<List<Post>> =
        db.getPost(TOP, page, count)
            .map { listPostsEntity ->
                listPostsEntity.map { it.toDomain() }
            }

    companion object {
        const val TOP = "top"
    }
}