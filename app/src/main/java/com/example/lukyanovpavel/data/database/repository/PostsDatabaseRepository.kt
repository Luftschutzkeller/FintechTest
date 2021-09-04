package com.example.lukyanovpavel.data.database.repository

import com.example.lukyanovpavel.data.api.dto.PostDto
import com.example.lukyanovpavel.data.api.dto.toEntity
import com.example.lukyanovpavel.data.database.DevLifeDatabase
import com.example.lukyanovpavel.data.database.entity.PostEntity
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

interface PostsDatabaseRepository {
    fun insertPost(
        category: String,
        posts: List<PostDto>,
        page: Int
    ): Completable

    fun getPost(
        category: String,
        page: Int,
        count: Int
    ): Single<List<PostEntity>>
}

class PostsDatabaseRepositoryImpl @Inject constructor(
    private val db: DevLifeDatabase
) : PostsDatabaseRepository {
    override fun insertPost(
        category: String,
        posts: List<PostDto>,
        page: Int
    ): Completable =
        Completable.fromAction {
            posts.forEachIndexed { index, post ->
                db.posts().insertPost(post.toEntity(category, page, index))
            }
        }
            .doOnError(Timber::e)

    override fun getPost(category: String, page: Int, count: Int): Single<List<PostEntity>> =
        db.posts().getPost(category, page, count)
            .doOnError(Timber::e)
            .firstOrError()
}