package com.example.lukyanovpavel.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.databinding.ScreenPostBinding
import com.example.lukyanovpavel.domain.common.ResourceState
import com.example.lukyanovpavel.ui.posts.bind
import com.example.lukyanovpavel.ui.posts.disableClickTemporarily
import com.example.lukyanovpavel.utils.error.NoInternetConnection
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseScreen<T : Any, VM : BaseViewModel<T>>(
    resourceId: Int
) : Fragment(resourceId) {
    private var _binding: ScreenPostBinding? = null
    private val binding get() = _binding!!
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
        initButtonsControl(vm)
    }

    private fun onStateReceive(resourceState: ResourceState<T>) {
        when (resourceState) {
            is ResourceState.Success -> handleSuccessState(resourceState.data)
            is ResourceState.Error -> handleErrorState(resourceState.error)
            is ResourceState.Loading -> handleLoadingState(true)
        }
    }

    private fun initButtonsControl(vm: VM) {
        with(binding) {
            postLayout.next.clicks()
                .subscribe {
                    vm.next()
                    postLayout.next.disableClickTemporarily()
                }

            postLayout.back.clicks()
                .subscribe {
                    vm.previous()
                    postLayout.back.disableClickTemporarily()
                }
        }
    }

    open fun handleSuccessState(data: T) {
        handleLoadingState(false)
        binding.postLayout.bind(data)
    }

    open fun handleLoadingState(state: Boolean) {
        with(binding) {
            postLayout.root.isVisible = !state
            progressBar.isVisible = state
        }
    }

    open fun handleErrorState(error: Throwable?) {
        handleLoadingState(false)
        with(binding) {
            errorLayout.root.visibility = View.VISIBLE
            errorLayout.bind(error)
            if (error is NoInternetConnection) {
                errorLayout.repeat.visibility = View.VISIBLE
                errorLayout.repeat.clicks()
                    .subscribe { repeat() }
            }
            postLayout.root.visibility = View.GONE
        }
    }

    abstract fun repeat()

    private fun subscribeFirstPostState(state: Observable<Boolean>) {
        state
            .doOnError(Timber::e)
            .subscribe(::handleFirstPositionState)
            .untilDestroy()
    }

    open fun handleFirstPositionState(state: Boolean) {
        binding.postLayout.back.isClickable = !state
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