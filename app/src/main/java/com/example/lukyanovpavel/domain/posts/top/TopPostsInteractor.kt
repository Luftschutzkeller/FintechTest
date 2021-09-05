package com.example.lukyanovpavel.domain.posts.top

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.PostCounter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface TopPostsInteractor {
    fun observPost(): Observable<Post>
    fun start(): Completable
    fun loadNext(): Completable
    fun loadPrevious(): Completable
    fun isFirstPosition(): Observable<Boolean>
}

class TopPostsInteractorImpl @Inject constructor(
    private val repo: TopPostsRepository
) : TopPostsInteractor {
    private val postStorage = BehaviorSubject.create<Post>()
    private val counter = PostCounter

    override fun observPost(): Observable<Post> = postStorage

    override fun start(): Completable =
        updateData()

    override fun loadNext(): Completable =
        counter.countPlus()
            .andThen(updateData())

    override fun loadPrevious(): Completable =
        counter.countMinus()
            .andThen(updateData())

    override fun isFirstPosition(): Observable<Boolean> = counter.isFirstPosition()

    private fun updateData(): Completable =
        with(counter) {
            repo.getPost(getNowPage(), getCount())
                .flatMapCompletable { post ->
                    updatePostStorage(post)
                }
                .doOnError(postStorage::onError)
        }

    private fun updatePostStorage(post: Post): Completable =
        Completable.fromAction {
            postStorage.onNext(post)
        }
}