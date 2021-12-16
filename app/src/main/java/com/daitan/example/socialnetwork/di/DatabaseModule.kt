package com.daitan.example.socialnetwork.di

import androidx.room.Room
import com.daitan.example.socialnetwork.model.common.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, AppDatabase.DB_NAME
        ).build()
    }
    single(createdAtStart = false) { get<AppDatabase>().postDao() }
}