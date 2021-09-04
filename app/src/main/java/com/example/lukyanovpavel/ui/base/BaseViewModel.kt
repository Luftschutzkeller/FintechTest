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
    protected val value = BehaviorSubject.create<ResourceState<T>>()
    protected val isFirstPosition = BehaviorSubject.create<Boolean>()

    protected fun onSetResource(resource: () -> Observable<T>): Completable =
        Completable.fromAction {
            try {
                resource.invoke()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(Timber::e)
                    .subscribe(
                        { success ->
                            value.onNext(ResourceState.Success(success))
                        },
                        { error ->
                            Timber.e(error)
                            value.onNext(ResourceState.Error(error))
                        }
                    )
                    .untilDestroy()
            } catch (error: Throwable) {
                Timber.e(error)
                value.onNext(ResourceState.Error(error))
            }
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