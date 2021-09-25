package com.example.lukyanovpavel.domain.common

import io.reactivex.Completable
import io.reactivex.Observable

interface ObjectStorage<T> {
    fun observ(): Observable<T>
    fun update(newObj: T): Completable
    fun error(error: Throwable)
}