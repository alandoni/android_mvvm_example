package com.daitan.example.socialnetwork.model.post

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PostService {
    @POST("post")
    suspend fun insertPost(@Body post: Post): Post

    @GET("post")
    suspend fun getPosts(): List<Post>
}
