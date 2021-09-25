package com.example.lukyanovpavel.domain.posts

import com.example.lukyanovpavel.domain.common.ObjectStorage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class PostStorage : ObjectStorage<Post> {
    private var count = 0
    private var page = 0
    private val isFirst = BehaviorSubject.create<Boolean>()
    private val coordinate = BehaviorSubject.create<Pair<Int, Int>>()
    private val post = BehaviorSubject.create<Post>()

    init {
        coordinate.onNext(page to count)
    }

    override fun observ(): Observable<Post> = post

    override fun update(newObj: Post): Completable =
        Completable.fromAction {
            post.onNext(newObj)
        }

    override fun error(error: Throwable) {
        post.onError(error)
    }

    fun countPlus(): Unit = plus()

    fun countMinus(): Unit = minus()

    fun postCoordinate(): Observable<Pair<Int, Int>> = coordinate

    fun isFirstPosition(): Observable<Boolean> = isFirst

    private fun plus() {
        if (count == 4) {
            count = 0
            page++
            coordinate.onNext(page to count)

        } else {
            count++
            coordinate.onNext(page to count)
        }
        isFirst.onNext(count == 0 && page == 0)
    }

    private fun minus() {
        if (count == 0 && page != 0) {
            count = 4
            page--
            coordinate.onNext(page to count)
        } else {
            if (count != 0) {
                count--
                coordinate.onNext(page to count)
            }
        }
        isFirst.onNext(count == 0 && page == 0)
    }
}