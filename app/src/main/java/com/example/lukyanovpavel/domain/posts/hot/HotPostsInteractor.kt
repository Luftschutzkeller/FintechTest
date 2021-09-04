package com.example.lukyanovpavel.domain.posts.hot

import com.example.lukyanovpavel.domain.common.ObjectStorage
import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

interface HotPostsInteractor {
    fun observPost(): Observable<Post>
    fun loadNext(): Completable
    fun loadPrevious(): Completable
    fun isFirstPosition(): Observable<Boolean>
}

class HotPostsInteractorImpl @Inject constructor(
    private val repo: HotPostsRepository,
    private val storage: ObjectStorage<Post>
) : HotPostsInteractor {

    override fun observPost(): Observable<Post> =
        updateData()
            .andThen(storage.observable())

    override fun loadNext(): Completable =
        storage.countPlus()
            .andThen(updateData())

    override fun loadPrevious(): Completable =
        storage.countMinus()
            .andThen(updateData())

    override fun isFirstPosition(): Observable<Boolean> =
        storage.isFirstPosition()

    private fun updateData(): Completable =
        with(storage) {
            repo.getPost(getNowPage(), getCount())
                .flatMapCompletable { post ->
                    if (post.isNotEmpty()) {
                        updateStorage(post.first())
                    } else {
                        loadNewPostsInNetwork(getNowPage())
                            .andThen(
                                repo.getPost(getNowPage(), getCount())
                                    .flatMapCompletable { newPost ->
                                        updateStorage(newPost.first())
                                    }
                            )
                    }
                }
        }

    private fun loadNewPostsInNetwork(page: Int): Completable =
        repo.load(page)
            .doOnError(storage::onError)
}