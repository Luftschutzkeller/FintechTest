package com.example.lukyanovpavel.ui.posts.top

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.databinding.ScreenTopBinding
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.base.BaseScreen
import com.example.lukyanovpavel.ui.posts.bind
import com.example.lukyanovpavel.utils.error.NoInternetConnection
import com.example.lukyanovpavel.utils.load
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScreenTop : BaseScreen<Post, ViewModelTop>(R.layout.screen_top) {
    private var _binding: ScreenTopBinding? = null
    private val binding get() = _binding!!
    private val vm: ViewModelTop by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenTopBinding.bind(view)
        onSubscribeVewModel(vm)
        vm.start()
        initUi()
    }

    override fun handleSuccessState(data: Post) {
        super.handleSuccessState(data)
        Timber.tag("ttt").d("handleSuccessState - ${data.gifURL}")
        binding.postLayout.postImage.load(data.gifURL)
    }

    override fun handleErrorState(error: Throwable?) {
        super.handleErrorState(error)
        with(binding) {
            errorLayout.root.visibility = View.VISIBLE
            errorLayout.bind(error)
            if (error is NoInternetConnection) {
                errorLayout.repeat.visibility = View.VISIBLE
            }
            postLayout.root.visibility = View.GONE
        }
    }

    override fun handleFirstPositionState(state: Boolean) {
        super.handleFirstPositionState(state)
        binding.postLayout.back.isClickable = !state
    }

    private fun initUi() {
        with(binding) {
            postLayout.next.clicks()
                .subscribe { vm.nextPost() }

            postLayout.back.clicks()
                .subscribe { vm.previousPost() }
        }
    }

    override fun onDestroy() {
        _binding = null
        vm.onDestroy()
        super.onDestroy()
    }
}