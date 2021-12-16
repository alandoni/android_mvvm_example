package com.daitan.example.socialnetwork.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daitan.example.socialnetwork.model.post.Post
import com.daitan.example.socialnetwork.model.post.PostRepository
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class MainActivityViewModel(private val postRepository: PostRepository): ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val text = MutableLiveData("")
    val posts = MutableLiveData<List<Post>>()
    val error = MutableLiveData<Exception>()

    fun createPost() = viewModelScope.launch {
        text.value?.let {
            loading.value = true
            val post = Post(user = "Alan", text = it)
            try {
                postRepository.sendPost(post)
            } catch (e: Exception) {
                error.value = e
            }
            loadPosts()
        }
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            posts.value = postRepository.getRemotePosts().also {
                if (it.isEmpty()) {
                    throw RuntimeException("Empty list")
                }
            }
        } catch (e: Exception) {
            error.value = e
        }
        loading.value = false
    }
}
