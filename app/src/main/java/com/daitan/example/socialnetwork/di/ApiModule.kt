package com.daitan.example.socialnetwork.di

import com.daitan.example.socialnetwork.model.post.PostService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(PostService::class.java) }
}