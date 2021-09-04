package com.example.lukyanovpavel.ui.posts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.base.BaseScreen
import com.example.lukyanovpavel.utils.load
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenTop : BaseScreen<Post, ViewModelPosts>(R.layout.screen_post) {
    private val vm: ViewModelPosts by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSubscribeVewModel(vm)
        vm.start("top")
        initUi()
    }

    override fun handleSuccessState(data: Post) {
        binding.postImage.load(data.gifURL)
    }

    private fun initUi() {
        with(binding) {
            next.clicks()
                .subscribe { vm.nextPost() }

            back.clicks()
                .subscribe { vm.previousPost() }
        }
    }

    override fun onDestroy() {
        vm.onDestroy()
        super.onDestroy()
    }
}