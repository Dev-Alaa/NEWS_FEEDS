package com.linkdevelpment.newsfeeds

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsFeedsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}