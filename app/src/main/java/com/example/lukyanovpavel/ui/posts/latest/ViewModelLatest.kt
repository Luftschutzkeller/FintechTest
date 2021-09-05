package com.example.lukyanovpavel.ui.posts.latest

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.latest.LatestPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelLatest @Inject constructor(
    private val repo: LatestPostsInteractor
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

    override fun next() {
        repo.loadNext()
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }

    override fun previous() {
        repo.loadPrevious()
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }
}