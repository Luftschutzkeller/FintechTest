package com.example.lukyanovpavel.domain.posts

import com.example.lukyanovpavel.data.repository.PostsRepository
import com.example.lukyanovpavel.domain.common.ObjectStorage
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

interface LoadPostsInteractor {
    fun observPost(): Observable<Post>
    fun setCategory(category: String): Completable
    fun loadNext(): Completable
    fun loadPrevious(): Completable
    fun isFirstPosition(): Observable<Boolean>
}

class LoadPostsInteractorImpl @Inject constructor(
    private val repo: PostsRepository,
    private val storage: ObjectStorage<Post>
) : LoadPostsInteractor {

    override fun observPost(): Observable<Post> =
        storage.observable()

    override fun setCategory(category: String): Completable =
        storage.updateCategory(category)
            .andThen(updateData())

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
            observCategory()
                .flatMapCompletable { cat ->
                    repo.getPost(cat, getNowPage(), getCount())
                        .flatMapCompletable { post ->
                            if (post.isNotEmpty()) {
                                updateStorage(post.first())
                            } else {
                                loadNewPostsInNetwork(cat, getNowPage())
                                    .andThen(
                                        repo.getPost(cat, getNowPage(), getCount())
                                            .flatMapCompletable { newPost ->
                                                updateStorage(newPost.first())
                                            }
                                    )
                            }
                        }
                }
        }

    private fun loadNewPostsInNetwork(category: String, page: Int): Completable =
        repo.loadPosts(category, page)
            .doOnError(storage::onError)
}