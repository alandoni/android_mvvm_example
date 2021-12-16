package com.daitan.example.socialnetwork.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daitan.example.socialnetwork.model.post.Post
import com.daitan.example.socialnetwork.model.post.PostRepository

class MainActivityViewModel(val postsRepository: PostRepository): ViewModel() {
    val text = MutableLiveData("")
    val posts = MutableLiveData<List<Post>>()

    fun createPost() {
        text.value?.let {
            loading.value = true
            val post = Post(user = "Alan", text = it)
            val list = posts.value?.toMutableList() ?: mutableListOf()
            list.add(post)
            posts.value = list
            loading.value = false
        }
    }
}
