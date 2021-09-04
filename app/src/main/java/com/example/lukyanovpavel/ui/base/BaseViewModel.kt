package com.example.lukyanovpavel.ui.base

import androidx.lifecycle.ViewModel
import com.example.lukyanovpavel.domain.common.ResourceState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

open class BaseViewModel<T : Any> : ViewModel() {
    private val dispose: CompositeDisposable = CompositeDisposable()
    protected val value = BehaviorSubject.create<ResourceState<T>>()
    protected val isFirstPosition = BehaviorSubject.create<Boolean>()
    protected val category = BehaviorSubject.create<String>()

    open fun onSetCategory(category: String) {
//        Timber.tag("ttt").d("category - $category")
        this.category.onNext(category)
    }

    open fun onSubscribeViewModel(): Observable<ResourceState<T>> = value

    open fun isFirstPosition(): Observable<Boolean> =
        isFirstPosition
            .doOnError(Timber::e)
            .flatMap { state ->
                Observable.create { emitter ->
                    emitter.onNext(state)
                }
            }

    protected fun Disposable.untilDestroy() {
        dispose.add(this)
    }

    open fun onDestroy() {
        dispose.clear()
    }
}