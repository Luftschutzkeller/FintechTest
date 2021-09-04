package com.example.lukyanovpavel.ui.base

import androidx.lifecycle.ViewModel
import com.example.lukyanovpavel.domain.common.ResourceState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

open class BaseViewModel<T : Any> : ViewModel() {
    private val dispose: CompositeDisposable = CompositeDisposable()
    protected val value = BehaviorSubject.create<T>()
    protected val isFirstPosition = BehaviorSubject.create<Boolean>()

    open fun onSubscribeViewModel(): Observable<ResourceState<T>> {
        return Observable.create { emitter ->
            value
                .doOnError { error ->
                    emitter.onNext(ResourceState.Error(error))
                    Timber.e(error)
                }
                .subscribe(
                    { success ->
                        emitter.onNext(ResourceState.Success(success))
                    },
                    { error ->
                        emitter.onNext(ResourceState.Error(error))
                    }
                )
                .untilDestroy()
        }
    }

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