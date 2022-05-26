package com.mvl.mvl_assignment.base

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import com.mvl.mvl_assignment.GPSBroadcastReceiver
import com.mvl.mvl_assignment.maphelper.map.ConnectivityReceiver
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class BaseApplication:Application() {

    private val gpsBroadcastReceiver = GPSBroadcastReceiver()
    override fun onCreate() {
        super.onCreate()
        registerReceiver(gpsBroadcastReceiver, IntentFilter("android.location.PROVIDERS_CHANGED"))
        Timber.plant(Timber.DebugTree())
        instance = this
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    override fun onTerminate() {
        super.onTerminate()

            unregisterReceiver(gpsBroadcastReceiver);

    }

    var context: Context? = null

    companion object {
        @get:Synchronized
        var instance: BaseApplication? = null
            private set
    }
}