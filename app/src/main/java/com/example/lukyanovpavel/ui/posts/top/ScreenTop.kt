package com.example.lukyanovpavel.ui.posts.top

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.base.BaseScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenTop : BaseScreen<Post, ViewModelTop>(R.layout.screen_post) {
    private val vm: ViewModelTop by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSubscribeVewModel(vm)
    }

    override fun repeat() {
        vm.repeat()
    }
}