package com.example.lukyanovpavel.domain.common

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class Counter {
    private var count = 0
    private var page = 0
    private val isFirst = BehaviorSubject.create<Boolean>()
    private val coordinate = BehaviorSubject.create<Pair<Int, Int>>()

    init {
        coordinate.onNext(page to count)
    }

    fun nextPost() = plus()

    fun previousPost() = minus()

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