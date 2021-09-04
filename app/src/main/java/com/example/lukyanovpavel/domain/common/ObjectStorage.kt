package com.example.lukyanovpavel.domain.common

import io.reactivex.Completable
import io.reactivex.Observable

interface ObjectStorage<T : Any> {
    fun updateStorage(value: T): Completable
    fun observable(): Observable<T>

    fun countPlus(): Completable
    fun countMinus(): Completable

    fun getCount(): Int
    fun getNowPage(): Int

    fun isFirstPosition(): Observable<Boolean>

    fun onError(error: Throwable)
}