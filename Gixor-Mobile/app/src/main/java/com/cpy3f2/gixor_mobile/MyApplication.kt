package com.cpy3f2.gixor_mobile

import PreferencesManager
import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        private var instance: MyApplication? = null
        lateinit var preferencesManager: PreferencesManager
            private set
        
        fun getInstance(): MyApplication {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
        
        fun getApplicationContext(): Context {
            return instance?.applicationContext 
                ?: throw IllegalStateException("Application context not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferencesManager = PreferencesManager(this)
    }
}
