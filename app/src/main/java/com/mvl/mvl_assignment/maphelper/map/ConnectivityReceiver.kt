package com.mvl.mvl_assignment.maphelper.map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.mvl.mvl_assignment.base.BaseApplication


class ConnectivityReceiver : BroadcastReceiver() {
    private val isConnected = false
    override fun onReceive(context: Context, arg1: Intent) {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = (activeNetwork != null
                && activeNetwork.isConnectedOrConnecting)
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        private const val LOG_TAG = "NetworkChangeReceiver"
        fun isConnected(): Boolean {
            val cm = BaseApplication.instance!!.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return (activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting)
        }
    }
}