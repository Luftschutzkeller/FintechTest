package com.example.lukyanovpavel.core.aplication

import android.app.Application
import com.example.lukyanovpavel.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBAG) {
            Timber.plant(Timber.DebugTree())
        }
        RxJavaPlugins.setErrorHandler(Timber::e)
    }
}