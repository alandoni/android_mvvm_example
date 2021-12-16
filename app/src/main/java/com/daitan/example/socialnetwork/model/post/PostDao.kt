package com.daitan.example.socialnetwork.model.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(post: List<Post>)

    @Query("SELECT * FROM posts ORDER BY date DESC")
    abstract suspend fun selectAll(): List<Post>

    @Query("SELECT * FROM posts WHERE id = :id")
    abstract suspend fun selectById(id: String): Post

    @Query("SELECT * FROM posts WHERE status = :status")
    abstract suspend fun selectByStatus(status: Post.PostStatus): List<Post>

    suspend fun selectFailed(): List<Post> = selectByStatus(Post.PostStatus.ERROR)
}