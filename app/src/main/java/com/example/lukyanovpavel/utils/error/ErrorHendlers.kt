package com.example.lukyanovpavel.utils.error

import com.example.lukyanovpavel.data.api.dto.PostsDto
import com.example.lukyanovpavel.utils.error.ErrorMessages.EMPTY_RESPONSE_ERROR
import com.example.lukyanovpavel.utils.error.ErrorMessages.NO_INTERNET_ERROR
import io.reactivex.Single

fun Single<PostsDto>.handleError(
    isAvailableNetworkState: Boolean
): Single<PostsDto> {
    if (!isAvailableNetworkState) {
        throw NoInternetConnection(NO_INTERNET_ERROR)
    } else {
        return this.handleEmptyListError()
    }
}

private fun Single<PostsDto>.handleEmptyListError(): Single<PostsDto> {
    return this.map { postsDto ->
        when (postsDto.result.isEmpty()) {
            true -> throw EmptyResponseError(EMPTY_RESPONSE_ERROR)
            else -> postsDto
        }
    }
}

object ErrorMessages {
    const val EMPTY_RESPONSE_ERROR = "Нет данных. Выберите другую категорию."
    const val NO_INTERNET_ERROR =
        "Произошла ошибка при загрузке данных. Проверьте подключение к сети."
}