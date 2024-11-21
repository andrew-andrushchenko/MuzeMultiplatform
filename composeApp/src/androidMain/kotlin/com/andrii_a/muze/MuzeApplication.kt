package com.andrii_a.muze

import android.app.Application
import com.andrii_a.muze.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MuzeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MuzeApplication)
            androidLogger()
        }
    }
}