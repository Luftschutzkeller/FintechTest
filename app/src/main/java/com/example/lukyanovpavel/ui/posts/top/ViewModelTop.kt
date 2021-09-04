package com.example.lukyanovpavel.ui.posts.top

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.top.TopPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelTop @Inject constructor(
    private val repo: TopPostsInteractor
) : BaseViewModel<Post>() {

    init {
        repo.isFirstPosition()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(isFirstPosition::onNext)
            .untilDestroy()
    }

    fun start() {
        onSetResource { repo.observPost() }
            .subscribe()
            .untilDestroy()
    }

    fun nextPost() {
        repo.loadNext()
            .subscribeOn(Schedulers.newThread())
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }

    fun previousPost() {
        repo.loadPrevious()
            .subscribeOn(Schedulers.newThread())
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }
}