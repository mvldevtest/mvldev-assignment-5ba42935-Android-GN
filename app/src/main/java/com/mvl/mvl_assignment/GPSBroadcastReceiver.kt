package com.mvl.mvl_assignment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager


class GPSBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //isGPSEnabled = true;
            } else {
                //isGPSEnabled = false;
            }
        } catch (ex: Exception) {
        }
    }
}