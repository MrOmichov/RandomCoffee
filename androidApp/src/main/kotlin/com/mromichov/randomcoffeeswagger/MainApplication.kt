package com.mromichov.randomcoffeeswagger

import android.app.Application
import com.mromichov.randomcoffeeswagger.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(allModules)
        }
    }
}