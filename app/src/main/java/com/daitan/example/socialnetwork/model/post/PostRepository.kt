package com.daitan.example.socialnetwork.model.post

class PostRepository(private val postDao: PostDao, private val postService: PostService) {
    suspend fun sendPost(post: Post): Post {
        return postService.insertPost(post)
    }

    suspend fun getRemotePosts(): List<Post> {
        return postService.getPosts()
    }
}