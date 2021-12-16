package com.daitan.example.socialnetwork.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.daitan.example.socialnetwork.view.MainActivityViewModel
import com.daitan.example.socialnetwork.model.post.PostRepository

val viewModelModule = module {
    factory { PostRepository(get(), get()) }
    viewModel { MainActivityViewModel(get()) }
}