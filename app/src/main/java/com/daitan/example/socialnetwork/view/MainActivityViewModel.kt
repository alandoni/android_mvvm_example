package com.daitan.example.socialnetwork.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daitan.example.socialnetwork.model.post.Post
import com.daitan.example.socialnetwork.model.post.PostRepository
import kotlinx.coroutines.async
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
            postRepository.createPostLocal(post)
            loadLocalPosts()
            postRepository.sendPost(post)
            loadPosts()
        }
    }

    fun loadPosts() = viewModelScope.launch {
        loading.value = false

        async {
            loadRemotePosts()
            sendFailedPosts()
        }

        loading.value = false
    }

    private suspend fun loadRemotePosts() {
        try {
            posts.value = postRepository.getRemotePosts().also {
                if (it.isEmpty()) {
                    throw RuntimeException("Empty list")
                }
            }
        } catch (e: Exception) {
            error.value = e
            loadLocalPosts()
        }
    }

    private suspend fun sendFailedPosts() {
        postRepository.sendFailedPosts()
        loadLocalPosts()
    }

    private suspend fun loadLocalPosts() {
        posts.value = postRepository.getLocalPosts()
    }
}
