package com.example.lukyanovpavel.domain.common

import io.reactivex.Completable
import io.reactivex.Observable

interface PostCounter {
    fun update(): Completable

    fun countPlus(): Completable
    fun countMinus(): Completable

    fun getCount(): Int
    fun getNowPage(): Int

    fun isFirstPosition(): Boolean
}