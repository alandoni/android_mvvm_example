package com.daitan.example.socialnetwork.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Post(
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)