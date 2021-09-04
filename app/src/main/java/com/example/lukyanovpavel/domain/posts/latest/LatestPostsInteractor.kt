package com.example.lukyanovpavel.domain.posts.latest

import com.example.lukyanovpavel.domain.posts.Counter
import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface LatestPostsInteractor {
    fun observPost(): Observable<Post>
    fun start(): Completable
    fun loadNext(): Completable
    fun loadPrevious(): Completable
    fun isFirstPosition(): Observable<Boolean>
}

class LatestPostsInteractorImpl @Inject constructor(
    private val repo: LatestPostsRepository
) : LatestPostsInteractor {
    private val postStorage = BehaviorSubject.create<Post>()
    private val isFirst = BehaviorSubject.create<Boolean>()
    private val storage = Counter

    init {
        isFirst.onNext(storage.isFirstPosition())
    }

    override fun observPost(): Observable<Post> = postStorage

    override fun start(): Completable =
        updateData()

    override fun loadNext(): Completable =
        storage.countPlus()
            .andThen(updateData())

    override fun loadPrevious(): Completable =
        storage.countMinus()
            .andThen(updateData())

    override fun isFirstPosition(): Observable<Boolean> = isFirst

    private fun updateData(): Completable =
        with(storage) {
            repo.getPost(getNowPage(), getCount())
                .flatMapCompletable { post ->
                    if (post.isNotEmpty()) {
                        updatePostStorage(post.first())
                    } else {
                        loadNewPostsInNetwork(getNowPage())
                            .andThen(
                                repo.getPost(getNowPage(), getCount())
                                    .flatMapCompletable { newPost ->
                                        updatePostStorage(newPost.first())
                                    }
                            )
                    }
                }
        }

    private fun updatePostStorage(post: Post): Completable =
        Completable.fromAction {
            postStorage.onNext(post)
            storage.update()
        }

    private fun loadNewPostsInNetwork(page: Int): Completable =
        repo.load(page)
            .doOnError(postStorage::onError)
}