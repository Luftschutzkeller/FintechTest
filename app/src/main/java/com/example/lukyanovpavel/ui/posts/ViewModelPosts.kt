package com.example.lukyanovpavel.ui.posts

import com.example.lukyanovpavel.domain.common.ResourceState
import com.example.lukyanovpavel.domain.posts.LoadPostsInteractor
import com.example.lukyanovpavel.domain.posts.Post
import com.example.lukyanovpavel.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelPosts @Inject constructor(
    private val repo: LoadPostsInteractor
) : BaseViewModel<Post>() {

    init {
        repo.isFirstPosition()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(isFirstPosition::onNext)
            .untilDestroy()
    }

    fun start(category: String) {
        Timber.tag("ttt").d("category - $category")
        onSetResource()
            .andThen(repo.setCategory(category))
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
                            value.onNext(ResourceState.Success(success))
                        },
                        { error ->
                            Timber.e(error)
                            value.onNext(ResourceState.Error(error))
                        }
                    )
                    .untilDestroy()
            } catch (error: Throwable) {
                Timber.e(error)
                value.onNext(ResourceState.Error(error))
            }
        }
}