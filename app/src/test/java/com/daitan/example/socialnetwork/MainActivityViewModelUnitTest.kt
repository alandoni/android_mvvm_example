package com.daitan.example.socialnetwork

import com.daitan.example.socialnetwork.model.post.Post
import com.daitan.example.socialnetwork.model.post.PostRepository
import com.daitan.example.socialnetwork.view.MainActivityViewModel
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityViewModelUnitTest: BaseTest() {

    private val mockRepository: PostRepository = mockk()

    @Test
    fun createPost_success() = runBlocking {
        val text = "Exemplo"
        val user = "Alan"

        val post = Post(user = user, text = text)

        coEvery { mockRepository.createPostLocal(any()) } answers { post }
        coEvery { mockRepository.getLocalPosts() } answers { listOf(post) }
        coEvery { mockRepository.sendPost(any()) } answers { post.status = Post.PostStatus.SENT }
        coEvery { mockRepository.getRemotePosts() } answers { listOf(post) }
        coEvery { mockRepository.sendFailedPosts() } answers { listOf() }

        val subject = MainActivityViewModel(mockRepository)
        subject.text.value = text
        subject.createPost().join()

        coVerifyOrder {
            mockRepository.createPostLocal(any())
            mockRepository.getLocalPosts()
            mockRepository.sendPost(any())
            mockRepository.getRemotePosts()
        }

        assertTrue((subject.posts.value ?: listOf()).first().id == post.id)
        assertTrue((subject.posts.value ?: listOf()).first().status == Post.PostStatus.SENT)
        assertTrue(subject.text.value == "")
    }
}