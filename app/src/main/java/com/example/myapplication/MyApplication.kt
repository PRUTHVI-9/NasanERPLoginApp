package com.example.myapplication

import android.app.Application
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("NASANAPP", "App is started!")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i("NASANAPP", "App is terminated!")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i("NASANAPP", "Low memory")
    }


}