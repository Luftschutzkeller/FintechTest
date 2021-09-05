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

abstract class BaseViewModel<T : Any> : ViewModel() {
    private val dispose: CompositeDisposable = CompositeDisposable()
    private val value = BehaviorSubject.create<T>()
    protected val isFirstPosition = BehaviorSubject.create<Boolean>()

    protected fun onSetResource(repo: () -> Observable<T>): Completable =
        Completable.fromAction {
            try {
                repo.invoke()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(Timber::e)
                    .subscribe(
                        { success ->
                            value.onNext(success)
                        },
                        { error ->
                            Timber.e(error)
                            value.onError(error)
                        }
                    )
                    .untilDestroy()
            } catch (error: Throwable) {
                Timber.e(error)
                value.onError(error)
            }
        }

    open fun onSubscribeViewModel(): Observable<ResourceState<T>> {
        return Observable.create { emitter ->
            value
                .doOnError { error ->
                    emitter.onNext(ResourceState.Error(error))
                    Timber.e(error)
                }
                .doOnSubscribe { emitter.onNext(ResourceState.Loading) }
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

    abstract fun next()

    abstract fun previous()

    protected fun Disposable.untilDestroy() {
        dispose.add(this)
    }

    open fun onDestroy() {
        dispose.clear()
    }
}