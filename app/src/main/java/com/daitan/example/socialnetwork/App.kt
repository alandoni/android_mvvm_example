package com.daitan.example.socialnetwork

import android.app.Application
import com.daitan.example.socialnetwork.di.apiModule
import com.daitan.example.socialnetwork.di.databaseModule
import com.daitan.example.socialnetwork.di.networkModule
import com.daitan.example.socialnetwork.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                apiModule,
                databaseModule,
                viewModelModule
            )
        }
    }
}