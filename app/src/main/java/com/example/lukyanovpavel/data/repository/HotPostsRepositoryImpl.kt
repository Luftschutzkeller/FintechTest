package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.repository.HotPostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.hot.HotPostsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HotPostsRepositoryImpl @Inject constructor(
    private val hotApi: HotPostsApiRepository,
    private val db: PostsDatabaseRepository
) : HotPostsRepository {

    override fun load(page: Int): Completable =
        hotApi.invoke(page)
            .flatMapCompletable { posts ->
                db.insertPost(HOT, posts, page)
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(page: Int, count: Int): Single<List<Post>> =
        db.getPost(HOT, page, count)
            .map { listPostsEntity ->
                listPostsEntity.map { it.toDomain() }
            }

    companion object {
        const val HOT = "hot"
    }
}