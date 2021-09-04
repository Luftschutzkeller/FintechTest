package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.repository.LatestPostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LatestPostsRepositoryImpl @Inject constructor(
    private val latestApi: LatestPostsApiRepository,
    private val db: PostsDatabaseRepository
) : LatestPostsRepository {

    override fun load(page: Int): Completable =
        latestApi.invoke(page)
            .flatMapCompletable { posts ->
                db.insertPost(LATEST, posts, page)
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(page: Int, count: Int): Single<List<Post>> =
        db.getPost(LATEST, page, count)
            .map { listPostsEntity ->
                listPostsEntity.map { it.toDomain() }
            }

    companion object {
        const val LATEST = "latest"
    }
}