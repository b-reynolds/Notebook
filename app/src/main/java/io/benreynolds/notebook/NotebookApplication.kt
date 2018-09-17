package io.benreynolds.notebook

import android.app.Application
import timber.log.Timber

class NotebookApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
