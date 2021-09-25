package com.example.lukyanovpavel.domain.posts.hot

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.PostStorage
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

interface HotPostsInteractor {
    fun observPost(): Observable<Post>
    fun start(): Completable
    fun loadNext()
    fun loadPrevious()
    fun isFirstPosition(): Observable<Boolean>
}

class HotPostsInteractorImpl @Inject constructor(
    private val repo: HotPostsRepository
) : HotPostsInteractor {
    private val counter = PostStorage()

    override fun observPost(): Observable<Post> = counter.observ()

    override fun start(): Completable =
        updateData()

    override fun loadNext() {
        counter.countPlus()
    }

    override fun loadPrevious() {
        counter.countMinus()
    }

    override fun isFirstPosition(): Observable<Boolean> = counter.isFirstPosition()

    private fun updateData(): Completable =
        counter.postCoordinate()
            .flatMapCompletable { coordinate ->
                repo.getPost(coordinate.first, coordinate.second)
                    .flatMapCompletable { post ->
                        counter.update(post)
                    }
                    .doOnError(counter::error)
            }
}