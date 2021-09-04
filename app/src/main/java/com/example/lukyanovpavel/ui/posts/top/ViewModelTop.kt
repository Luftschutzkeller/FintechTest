package com.example.lukyanovpavel.ui.posts.top

import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.domain.posts.top.TopPostsInteractor
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
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
        onSetResource()
            .andThen(repo.start())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
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

    private fun onSetResource(): Completable =
        Completable.fromAction {
            try {
                repo.observPost()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(Timber::e)
                    .subscribe(
                        { success ->
                            Timber.tag("ttt").d("onSetResource() - ${success.gifURL}")
                            value.onNext(success)
                        },
                        { error ->
                            Timber.e(error)
                            value.onError(error)
                        }
                    )
                    .untilDestroy()
            } catch (error: Throwable) {
                Timber.e(error)
                value.onError(error)
            }
        }
}