package com.example.lukyanovpavel.domain.posts

import com.example.lukyanovpavel.domain.common.Counter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object PostCounter : Counter {
    private var count = 0
    private var nowPage = 0
    private var isFirst = BehaviorSubject.create<Boolean>()

    override fun countPlus(): Completable = plus()

    override fun countMinus(): Completable = minus()

    override fun getCount() = count

    override fun getNowPage() = nowPage

    override fun isFirstPosition(): Observable<Boolean> = isFirst

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