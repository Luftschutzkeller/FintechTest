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
            .andThen(repo.start())
            .subscribe()
            .untilDestroy()
    }

    override fun next() {
        super.next()
        repo.loadNext()
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }

    override fun previous() {
        super.previous()
        repo.loadPrevious()
            .doOnError(Timber::e)
            .subscribe()
            .untilDestroy()
    }
}