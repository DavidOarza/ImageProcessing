package com.university.ip

import android.app.Application
import android.content.Context

class IPApplication : Application() {

    companion object {
        lateinit var instance: IPApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun get(context: Context): IPApplication {
        return context.applicationContext as IPApplication
    }
}