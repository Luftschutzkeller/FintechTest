package com.example.lukyanovpavel.data.repository

import com.example.lukyanovpavel.data.api.repository.PostsApiRepository
import com.example.lukyanovpavel.data.database.entity.toDomain
import com.example.lukyanovpavel.data.database.repository.PostsDatabaseRepository
import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

interface PostsRepository {
    fun loadPosts(category: String, page: Int): Completable

    fun getPost(
        category: String,
        page: Int,
        count: Int
    ): Single<List<Post>>
}

class PostsRepositoryImpl @Inject constructor(
    private val api: PostsApiRepository,
    private val db: PostsDatabaseRepository
) : PostsRepository {

    override fun loadPosts(category: String, page: Int): Completable =
        api.invoke(category, page)
            .flatMapCompletable { posts ->
                db.insertPost(category, posts, page)
            }
            .doOnError(Timber::e)
            .subscribeOn(Schedulers.io())

    override fun getPost(category: String, page: Int, count: Int): Single<List<Post>> =
        db.getPost(category, page, count)
            .map { listPostsEntity ->
                listPostsEntity.map { it.toDomain() }
            }
}