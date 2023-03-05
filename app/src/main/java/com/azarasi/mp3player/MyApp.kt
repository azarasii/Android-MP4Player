package com.azarasi.mp3player

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        myApp = this
    }

    companion object {
        lateinit var myApp: MyApp
    }
}