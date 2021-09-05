package com.example.lukyanovpavel.data.api.services

import com.example.lukyanovpavel.data.api.dto.PostsDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {
    @GET("top/{number}?json=true")
    fun getTop(
        @Path("number") number: Int
    ): Single<PostsDto>

    @GET("hot/{number}?json=true")
    fun getHot(
        @Path("number") number: Int
    ): Single<PostsDto>

    @GET("latest/{number}?json=true")
    fun getLatest(
        @Path("number") number: Int
    ): Single<PostsDto>
}