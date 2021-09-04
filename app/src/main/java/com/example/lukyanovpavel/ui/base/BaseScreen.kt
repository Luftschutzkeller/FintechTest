package com.example.lukyanovpavel.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lukyanovpavel.databinding.ScreenPostBinding
import com.example.lukyanovpavel.domain.common.ResourceState
import com.example.lukyanovpavel.ui.posts.bind
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseScreen<T : Any, VM : BaseViewModel<T>>(
    resourceId: Int
) : Fragment(resourceId) {
    private var _binding: ScreenPostBinding? = null
    protected val binding get() = _binding!!
    private val dispose = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenPostBinding.bind(view)
    }

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

    open fun handleSuccessState(data: T) {}

    open fun handleErrorState(error: Throwable?) {
        Timber.tag("ttt").d("handleErrorState - ${error?.message}")
        binding.errorLayout.root.visibility = View.VISIBLE
        binding.errorLayout.bind(error)
    }

    private fun subscribeFirstPostState(state: Observable<Boolean>) {
        state
            .doOnError(Timber::e)
            .subscribe(::handleFirstPositionState)
            .untilDestroy()
    }

    open fun handleFirstPositionState(state: Boolean) {
        binding.back.isClickable = !state
    }

    private fun Disposable.untilDestroy() {
        dispose.add(this)
    }

    override fun onDestroy() {
        dispose.clear()
        _binding = null
        super.onDestroy()
    }
}