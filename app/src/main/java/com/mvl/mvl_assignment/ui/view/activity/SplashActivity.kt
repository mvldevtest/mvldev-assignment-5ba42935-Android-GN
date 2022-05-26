package com.mvl.mvl_assignment.ui.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mvl.mvl_assignment.R
import com.mvl.mvl_assignment.helper.PrefDelegate
import com.mvl.mvl_assignment.helper.SharePrefStorage
import com.mvl.mvl_assignment.helper.clearPrefDelegate
import com.mvl.mvl_assignment.helper.showToast


class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 1000
    var wentToSettingsPage: Boolean? = false
    var sharePrefStorage: SharePrefStorage = SharePrefStorage()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        PrefDelegate.init(this)
        sharePrefStorage.latBVAlue = ""
        sharePrefStorage.latAValue = ""
        sharePrefStorage.lngBValue = ""
        sharePrefStorage.lngAValue = ""
        sharePrefStorage.mapQAValue = ""
        sharePrefStorage.mapQBValue = ""
        sharePrefStorage.mapALocation = ""
        sharePrefStorage.mapBLocation = ""
        sharePrefStorage.Loc_NickName_A = ""
        sharePrefStorage.Loc_NickName_b = ""
        //check for permission in android version greater than 6.0
        checkPermission()
        clearPrefDelegate()
    }

    private fun goToNextScreen() {
        Handler().postDelayed(
            {
                val intent = Intent(this@SplashActivity, MapActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT
        )
    }


    private fun checkPermission() {
        Dexter.withContext(this@SplashActivity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            //toast("OK")
                            val manager =
                                getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                buildAlertMessageNoGps()
                            } else {
                                goToNextScreen()
                            }

                        } else {
                            showDeniedPermissionNeededDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    // Remember to invoke this method when the custom rationale is closed
                    // or just by default if you don't want to use any custom rationale.
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                showToast(it.name)
                showDeniedPermissionNeededDialog()
            }
            .check()
    }


    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                checkPermission()
            }
            .setNegativeButton(
                "No"

            ) { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert = builder.create()
        alert.show()
    }

    /**
     * Show denied dialog
     */
    fun showDeniedPermissionNeededDialog() {
        wentToSettingsPage = true
        AlertDialog.Builder(this)
            .setMessage("You need to allow permissions to enter into the app")
            .setPositiveButton("Allow") { dialogInterface, i -> requestingPermissions() }
            .setNegativeButton("Cancel") { dialogInterface, i -> finish() }
            .setCancelable(false).create().show()
    }


    /**
     * Request Permission again
     */
    fun requestingPermissions() {
        checkPermission()
    }
}
