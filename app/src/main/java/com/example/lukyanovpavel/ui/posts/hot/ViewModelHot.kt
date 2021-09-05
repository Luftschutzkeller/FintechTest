package com.example.lukyanovpavel.ui.posts.hot

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.hot.HotPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelHot @Inject constructor(
    private val repo: HotPostsInteractor
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
            .andThen(repo.start())
            .subscribe()
            .untilDestroy()
    }

    fun nextPost() {
        repo.loadNext()
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }

    fun previousPost() {
        repo.loadPrevious()
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }
}