package com.daitan.example.socialnetwork

import com.daitan.example.socialnetwork.model.post.Post
import com.daitan.example.socialnetwork.model.post.PostRepository
import com.daitan.example.socialnetwork.view.MainActivityViewModel
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test

import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityViewModelUnitTest: BaseTest() {

    private val mockRepository: PostRepository = mockk()

    @Test
    fun createPost_failure() = runBlocking {
        createPostWithResult(Post.PostStatus.ERROR)
    }

    @Test
    fun createPost_success() = runBlocking {
        createPostWithResult(Post.PostStatus.SENT)
    }

    private suspend fun createPostWithResult(status: Post.PostStatus) {
        val text = "Olá"
        val name = "user"
        val post = Post(user = name, text = text)

        coEvery { mockRepository.createPostLocal(any()) } answers { post }
        coEvery { mockRepository.getLocalPosts() }.answers { listOf(post) }
        coEvery { mockRepository.sendPost(any()) } answers { post.status = status }
        coEvery { mockRepository.getRemotePosts() }.answers { listOf(post) }
        coEvery { mockRepository.sendFailedPosts() }.answers { listOf() }

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
        assertTrue((subject.posts.value ?: listOf()).first().status == status)
        assertEquals(subject.text.value, "")
    }

    @Test
    fun loadPostsWithEmptyList() = runBlocking {
        coEvery { mockRepository.sendFailedPosts() } returns emptyList()
        coEvery { mockRepository.getRemotePosts() } returns emptyList()
        coEvery { mockRepository.getLocalPosts() } returns emptyList()

        val subject = MainActivityViewModel(mockRepository)
        subject.loadPosts()

        coVerifyAll {
            mockRepository.sendFailedPosts()
            mockRepository.getLocalPosts()
            mockRepository.getRemotePosts()
        }

        assertEquals(subject.posts.value?.isEmpty(), true)
        assertEquals(subject.isCacheOnly.value, true)
        assertEquals(subject.error.value?.message, "Empty List")
    }

    @Test
    fun loadPostsWithRemoteResults() = runBlocking {
        val list = listOf(
            Post(user = "user", text = "Olá")
        )
        list.first().status = Post.PostStatus.SENT
        coEvery { mockRepository.sendFailedPosts() } returns emptyList()
        coEvery { mockRepository.getRemotePosts() } returns list
        coEvery { mockRepository.getLocalPosts() } returns emptyList()

        val subject = MainActivityViewModel(mockRepository)
        subject.loadPosts()

        coVerifyAll {
            mockRepository.sendFailedPosts()
            mockRepository.getLocalPosts()
            mockRepository.getRemotePosts()
        }

        assertEquals(subject.posts.value?.isEmpty(), false)
        assertEquals(subject.isCacheOnly.value, false)
        assertNull(subject.error.value)
    }

    @Test
    fun loadPostsWithCacheResults() = runBlocking {
        val list = listOf(
            Post(user = "user", text = "Olá")
        )
        list.first().status = Post.PostStatus.SENT
        coEvery { mockRepository.sendFailedPosts() } returns emptyList()
        coEvery { mockRepository.getRemotePosts() } answers {
            throw HttpException(
                Response.error<String>(
                    400,
                    "Bad Request".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )
        }
        coEvery { mockRepository.getLocalPosts() } returns list

        val subject = MainActivityViewModel(mockRepository)
        subject.loadPosts()

        coVerifyAll {
            mockRepository.sendFailedPosts()
            mockRepository.getLocalPosts()
            mockRepository.getRemotePosts()
        }

        assertEquals(subject.posts.value?.isEmpty(), false)
        assertEquals(subject.isCacheOnly.value, true)
        assertNotNull(subject.error.value)
    }
}