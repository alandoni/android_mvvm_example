package com.daitan.example.socialnetwork.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.daitan.example.socialnetwork.model.common.DatabaseDateConverter
import java.util.*

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val user: String,
    val text: String = "",
    @TypeConverters(DatabaseDateConverter::class) val date: Date = Date(),
    var status: PostStatus = PostStatus.SENDING
) {
    enum class PostStatus {
        SENDING, ERROR, RECEIVED, SENT
    }
}