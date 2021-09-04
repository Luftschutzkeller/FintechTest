package com.example.lukyanovpavel.ui.base

import androidx.fragment.app.Fragment
import com.example.lukyanovpavel.domain.common.ResourceState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseScreen<T : Any, VM : BaseViewModel<T>>(
    resourceId: Int
) : Fragment(resourceId) {
    private val dispose = CompositeDisposable()

    protected fun onSubscribeVewModel(vm: VM) {
        vm.onSubscribeViewModel()
            .doOnError(Timber::e)
            .subscribe(::onStateReceive)
            .untilDestroy()

        subscribeFirstPostState(vm.isFirstPosition())
    }

    private fun onStateReceive(resourceState: ResourceState<T>) {
        when (resourceState) {
            is ResourceState.Success -> handleSuccessState(resourceState.data)
            is ResourceState.Error -> handleErrorState(resourceState.error)
        }
    }

    open fun handleSuccessState(data: T) {
        Timber.tag("ttt").d("handleSuccessStateBase - $data")
    }

    open fun handleErrorState(error: Throwable?) {
        Timber.tag("ttt").d("handleErrorState - ${error?.message}")
    }

    private fun subscribeFirstPostState(state: Observable<Boolean>) {
        state
            .doOnError(Timber::e)
            .subscribe(::handleFirstPositionState)
            .untilDestroy()
    }

    open fun handleFirstPositionState(state: Boolean) {}

    private fun Disposable.untilDestroy() {
        dispose.add(this)
    }

    override fun onDestroy() {
        dispose.clear()
        super.onDestroy()
    }
}