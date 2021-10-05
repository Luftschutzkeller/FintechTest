package com.example.lukyanovpavel.ui.posts.hot

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.hot.HotPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelHot @Inject constructor(
    private val repo: HotPostsInteractor
) : BaseViewModel<Post>() {
    init {
        onSetResource { repo.observPost() }

        onSetFirstPosition { repo.isFirstPosition() }
    }

    override fun next() {
        repo.loadNext()
    }

    override fun previous() {
        repo.loadPrevious()
    }

    override fun repeat() {
        onSetResource { repo.observPost() }
    }
}