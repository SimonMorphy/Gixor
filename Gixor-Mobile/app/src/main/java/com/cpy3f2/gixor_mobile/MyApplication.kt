package com.cpy3f2.gixor_mobile

import android.app.Application
import android.content.Context


class MyApplication : Application() {
    companion object {
        var instance: Application? = null
        
        fun getApplicationContext(): Context {
            return instance?.applicationContext 
                ?: throw IllegalStateException("Application context not initialized")
        }
        
        fun initializeApp(context: Context) {
            if (instance == null) {
                instance = MyApplication()
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}
