package com.example.lukyanovpavel.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lukyanovpavel.domain.common.ResourceState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class BaseViewModel<T : Any> : ViewModel() {
    private val dispose: CompositeDisposable = CompositeDisposable()
    private val _post = MutableLiveData<ResourceState<T>>()
    private val _isFirstPosition = MutableLiveData<Boolean>()

    val post: LiveData<ResourceState<T>> get() = _post
    val isFirstPosition: LiveData<Boolean> get() = _isFirstPosition

    protected fun onSetResource(repo: () -> Observable<T>): Completable =
        Completable.fromAction {
            try {
                repo.invoke()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(Timber::e)
                    .subscribe(
                        { success ->
                            _post.postValue(ResourceState.Success(success))
                        },
                        { error ->
                            Timber.e(error)
                            _post.postValue(ResourceState.Error(error))
                        }
                    )
                    .untilDestroy()
            } catch (error: Throwable) {
                Timber.e(error)
                _post.postValue(ResourceState.Error(error))
            }
        }

    protected fun onSetFirstPosition(isFirstPosition: () -> Observable<Boolean>) {
        isFirstPosition.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_isFirstPosition::postValue)
            .untilDestroy()
    }

    abstract fun next()

    abstract fun previous()

    abstract fun repeat()

    protected fun Disposable.untilDestroy() {
        dispose.add(this)
    }

    override fun onCleared() {
        dispose.clear()
        super.onCleared()
    }
}