package com.example.lukyanovpavel.domain.posts

import com.example.lukyanovpavel.domain.common.PostCounter
import io.reactivex.Completable

object Counter : PostCounter {
    private var count = 0
    private var nowPage = 0
    private var isFirst = true

    override fun update(): Completable {
        return Completable.fromAction {
            isFirst = count == 0 && nowPage == 0
        }
    }

    override fun countPlus(): Completable = plus()

    override fun countMinus(): Completable = minus()

    override fun getCount() = count

    override fun getNowPage() = nowPage

    override fun isFirstPosition(): Boolean = isFirst

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
            isFirst = count == 0 && nowPage == 0
        }
}