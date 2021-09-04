package com.example.lukyanovpavel.data.api.services

import com.example.lukyanovpavel.data.api.dto.PostsDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {
    @GET("{page}/{number}?json=true")
    fun getPosts(
        @Path("page") page: String,
        @Path("number") number: Int,
    ): Single<PostsDto>
}