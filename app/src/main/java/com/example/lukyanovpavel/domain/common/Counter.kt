package com.example.lukyanovpavel.domain.common

import io.reactivex.Completable
import io.reactivex.Observable

interface Counter {
    fun countPlus(): Completable
    fun countMinus(): Completable

    fun getCount(): Int
    fun getNowPage(): Int

    fun isFirstPosition(): Observable<Boolean>
}