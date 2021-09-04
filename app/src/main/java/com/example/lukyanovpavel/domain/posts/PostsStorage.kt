package com.example.lukyanovpavel.domain.posts

import com.example.lukyanovpavel.domain.common.ObjectStorage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object PostsStorage : ObjectStorage<Post> {
    private val post = BehaviorSubject.create<Post>()
    private val isFirst = BehaviorSubject.create<Boolean>()
    private var count = 0
    private var nowPage = 0

    override fun updateStorage(value: Post): Completable {
        return Completable.fromAction {
            post.onNext(value)
            isFirst.onNext(count == 0 && nowPage == 0)
        }
    }

    override fun observable(): Observable<Post> = post

    override fun countPlus(): Completable = plus()

    override fun countMinus(): Completable = minus()

    override fun getCount() = count

    override fun getNowPage() = nowPage

    override fun isFirstPosition(): Observable<Boolean> = isFirst

    override fun onError(error: Throwable) {
        post.onError(error)
    }

    private fun plus(): Completable =
        Completable.fromAction {
            if (count == 4) {
                count = 0
                nowPage++
            } else {
                count++
            }
        }

    private fun minus(): Completable =
        Completable.fromAction {
            if (count == 0 && nowPage != 0) {
                count = 4
                nowPage--
            } else {
                if (count != 0) count--
            }
            isFirst.onNext(count == 0 && nowPage == 0)
        }
}