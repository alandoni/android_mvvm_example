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
    val error = MutableLiveData<Exception?>()
    val isCacheOnly = MutableLiveData<Boolean>()

    fun createPost() = viewModelScope.launch {
        text.value?.let {
            loading.value = true
            val post = Post(user = "Alan", text = it)
            postRepository.createPostLocal(post)
            loadLocalPosts()
            postRepository.sendPost(post)
            loadPosts()
        }
        text.value = ""
    }

    fun loadPosts() = viewModelScope.launch {

        error.value = null
        isCacheOnly.value = false

        async {
            loading.value = true

            sendFailedPosts()
            loadRemotePosts()

            loading.value = false
        }
    }

    private suspend fun loadRemotePosts() {
        try {
            isCacheOnly.value = false
            posts.value = postRepository.getRemotePosts().also {
                if (it.isEmpty()) {
                    throw RuntimeException("Empty List")
                }
            }
        } catch (e: Exception) {
            when (e) {
                is RuntimeException ->
                    error.value = e
                else -> {
                    error.value = e
                    loadLocalPosts()
                    isCacheOnly.value = true
                }
            }
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
