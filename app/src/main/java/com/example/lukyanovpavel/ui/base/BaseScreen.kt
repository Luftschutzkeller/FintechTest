package com.example.lukyanovpavel.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.lukyanovpavel.databinding.ScreenPostBinding
import com.example.lukyanovpavel.domain.common.ResourceState
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.posts.bind
import com.example.lukyanovpavel.utils.error.NoInternetConnection

abstract class BaseScreen<T : Any, VM : BaseViewModel<T>>(
    resourceId: Int
) : Fragment(resourceId) {
    private var _binding: ScreenPostBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenPostBinding.bind(view)
    }

    protected fun onSubscribeVewModel(vm: VM) {
        vm.post.observe(viewLifecycleOwner, (::onStateReceive))
        vm.isFirstPosition.observe(viewLifecycleOwner, (::handleFirstPositionState))
        initButtonsControl(vm)
    }

    private fun onStateReceive(resourceState: ResourceState<T>) {
        when (resourceState) {
            is ResourceState.Success -> handleSuccessState(resourceState.data)
            is ResourceState.Error -> handleErrorState(resourceState.error, true)
            is ResourceState.Loading -> handleLoadingState(true)
        }
    }

    private fun initButtonsControl(vm: VM) {
        with(binding) {
            postLayout.next.setOnClickListener { vm.next() }

            postLayout.back.setOnClickListener { vm.previous() }
        }
    }

    private fun handleSuccessState(data: T) {
        handleLoadingState(false)
        handleErrorState(state = false)
        when (data) {
            is Post -> binding.postLayout.bind(data)
        }
    }

    private fun handleLoadingState(state: Boolean) {
        with(binding) {
            postLayout.root.isVisible = !state
            progressBar.isVisible = state
        }
    }

    private fun handleErrorState(error: Throwable? = null, state: Boolean) {
        handleLoadingState(false)
        with(binding) {
            errorLayout.root.isVisible = state
            errorLayout.bind(error)
            if (error is NoInternetConnection) {
                errorLayout.repeat.isVisible = state
                errorLayout.repeat.setOnClickListener { repeat() }
            }
            postLayout.root.isVisible = !state
        }
    }

    abstract fun repeat()

    private fun handleFirstPositionState(state: Boolean) {
        binding.postLayout.back.isClickable = !state
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}