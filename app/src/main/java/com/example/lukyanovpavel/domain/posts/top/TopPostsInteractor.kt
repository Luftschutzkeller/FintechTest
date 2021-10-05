package com.example.lukyanovpavel.domain.posts.top

import com.example.lukyanovpavel.domain.common.Counter
import com.example.lukyanovpavel.domain.posts.Post
import io.reactivex.Observable
import javax.inject.Inject

interface TopPostsInteractor {
    fun observPost(): Observable<Post>
    fun loadNext()
    fun loadPrevious()
    fun isFirstPosition(): Observable<Boolean>
}

class TopPostsInteractorImpl @Inject constructor(
    private val repo: TopPostsRepository
) : TopPostsInteractor {
    private val counter = Counter()

    override fun observPost(): Observable<Post> = updateData()

    override fun loadNext() {
        counter.nextPost()
    }

    override fun loadPrevious() {
        counter.previousPost()
    }

    override fun isFirstPosition(): Observable<Boolean> = counter.isFirstPosition()

    private fun updateData(): Observable<Post> =
        counter.postCoordinate()
            .flatMapSingle { coordinate ->
                repo.getPost(coordinate.first, coordinate.second)
            }
}