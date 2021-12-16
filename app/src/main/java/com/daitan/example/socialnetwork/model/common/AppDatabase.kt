package com.daitan.example.socialnetwork.model.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daitan.example.socialnetwork.model.post.Post
import com.daitan.example.socialnetwork.model.post.PostDao

@Database(
    entities = [Post::class],
    version = AppDatabase.DB_VERSION,
    exportSchema = false
)
@TypeConverters(DatabaseDateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "socialnetwork.db"
    }
}