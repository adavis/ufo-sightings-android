package info.adavis.ufosightings

import android.app.Application
import timber.log.Timber

class UFOSightingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}