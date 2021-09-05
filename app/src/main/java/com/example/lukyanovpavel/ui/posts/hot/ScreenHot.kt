package com.example.lukyanovpavel.ui.posts.hot

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.base.BaseScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenHot : BaseScreen<Post, ViewModelHot>(R.layout.screen_post) {
    private val vm: ViewModelHot by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSubscribeVewModel(vm)
        vm.start()
    }

    override fun repeat() {
        vm.start()
    }

    override fun onDestroy() {
        vm.onDestroy()
        super.onDestroy()
    }
}