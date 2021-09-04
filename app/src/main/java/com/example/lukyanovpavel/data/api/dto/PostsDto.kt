package com.example.lukyanovpavel.data.api.dto

import android.os.Parcelable
import com.example.lukyanovpavel.data.database.entity.PostEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
class PostsDto(
    @SerialName("result") val result: List<PostDto>,
    @SerialName("totalCount") val totalCount: Int
) : Parcelable

@Parcelize
class PostDto(
    @SerialName("id") val id: Int,
    @SerialName("description") val description: String,
    @SerialName("votes") val votes: Int,
    @SerialName("author") val author: String,
    @SerialName("date") val date: String,
    @SerialName("gifURL") val gifURL: String,
    @SerialName("gifSize") val gifSize: Int,
    @SerialName("previewURL") val previewURL: String,
    @SerialName("videoURL") val videoURL: String,
    @SerialName("videoPath") val videoPath: String,
    @SerialName("videoSize") val videoSize: Int,
    @SerialName("type") val type: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("commentsCount") val commentsCount: Int,
    @SerialName("fileSize") val fileSize: Int,
    @SerialName("canVote") val canVote: Boolean
) : Parcelable

fun PostDto.toEntity(
    category: String,
    page: Int,
    count: Int
): PostEntity =
    PostEntity(
        id = id,
        page = page,
        count = count,
        category = category,
        description = description,
        gifURL = gifURL
    )