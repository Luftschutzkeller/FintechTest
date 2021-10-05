package com.example.lukyanovpavel.ui.posts.latest

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelLatest @Inject constructor(
    private val repo: LatestPostsInteractor
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