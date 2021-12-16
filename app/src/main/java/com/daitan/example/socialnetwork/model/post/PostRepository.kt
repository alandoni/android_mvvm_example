package com.daitan.example.socialnetwork.model.post

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class PostRepository(private val postDao: PostDao, private val postService: PostService) {

    suspend fun createPostLocal(post: Post): Post {
        postDao.insert(post)
        return postDao.selectById(post.id)
    }

    suspend fun sendPost(post: Post) {
        try {
            val remotePost = postService.insertPost(post)
            remotePost.status = Post.PostStatus.SENT
            postDao.insert(remotePost)
        } catch (e: Exception) {
            post.status = Post.PostStatus.ERROR
            postDao.insert(post)
        }
    }

    suspend fun getRemotePosts(): List<Post> {
        val posts = postService.getPosts()
        postDao.insertAll(posts)
        return getLocalPosts()
    }

    suspend fun getLocalPosts(): List<Post> {
        return postDao.selectAll()
    }

    suspend fun sendFailedPosts() = withContext(Dispatchers.Default) {
        val failed = postDao.selectFailed()
        failed.map {
            async {
                sendPost(it)
            }
        }.awaitAll()
    }
}