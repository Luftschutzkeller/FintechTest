package com.example.lukyanovpavel.ui.posts.top

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.top.TopPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelTop @Inject constructor(
    private val repo: TopPostsInteractor
) : BaseViewModel<Post>() {
    init {
        onSetResource { repo.observPost() }
            .andThen(repo.start())
            .subscribe()
            .untilDestroy()

        onSetFirstPosition { repo.isFirstPosition() }
    }

    override fun next() {
        repo.loadNext()
    }

    override fun previous() {
        repo.loadPrevious()
    }

    override fun repeat() {
        repo.start()
            .subscribe()
            .untilDestroy()
    }
}