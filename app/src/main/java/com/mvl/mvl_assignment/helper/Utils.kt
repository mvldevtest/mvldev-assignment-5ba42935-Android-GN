package com.mvl.mvl_assignment.helper


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.material.snackbar.Snackbar
import com.mvl.mvl_assignment.BuildConfig

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import java.net.NetworkInterface


/**
 * Readable naming convention for Network call lambda
 * @since 1.0
 */
typealias NetworkAPIInvoke<T> = suspend () -> Response<T>

/**
 * typealias for lambda passed when a photo is tapped on in Popular Photos Fragment
 */
typealias ListItemClickListener<T> = (T) -> Unit

/**
 * Sealed class type-restricts the result of IO calls to success and failure. The type
 * <T> represents the model class expected from the API call in case of a success
 * In case of success, the result will be wrapped around the OnSuccessResponse class
 * In case of error, the throwable causing the error will be wrapped around OnErrorResponse class
 * @author Prasan
 * @since 1.0
 */
sealed class IOTaskResult<out DTO : Any> {
    data class OnSuccess<out DTO : Any>(val data: DTO) : IOTaskResult<DTO>()
    data class OnFailed(val throwable: Throwable) : IOTaskResult<Nothing>()
}

/**
 * Utility function that works to perform a Retrofit API call and return either a success model
 * instance or an error message wrapped in an [Exception] class
 * @param messageInCaseOfError Custom error message to wrap around [IOTaskResult.OnFailed]
 * with a default value provided for flexibility
 * @param networkApiCall lambda representing a suspend function for the Retrofit API call
 * @return [IOTaskResult.OnSuccess] object of type [T], where [T] is the success object wrapped around
 * [IOTaskResult.OnSuccess] if network call is executed successfully, or [IOTaskResult.OnFailed]
 * object wrapping an [Exception] class stating the error
 * @since 1.0
 */
@ExperimentalCoroutinesApi
suspend fun <T : Any> performSafeNetworkApiCall(
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    networkApiCall: NetworkAPIInvoke<T>
): Flow<IOTaskResult<T>> {
    var delayDuration = 1000L
    val delayFactor = 2
    return flow {
        val response = networkApiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(IOTaskResult.OnSuccess(it))
            }
                ?: emit(IOTaskResult.OnFailed(IOException("API call successful but empty response body")))
            return@flow
        }
        emit(
            IOTaskResult.OnFailed(
                IOException(
                    "API call failed with error - ${
                        response.errorBody()
                            ?.string() ?: messageInCaseOfError
                    }"
                )
            )
        )
        return@flow
    }.catch { e ->
        emit(IOTaskResult.OnFailed(IOException("Exception during network API call: ${e.message}")))
        return@catch
    }.retryWhen { cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.flowOn(Dispatchers.IO)
}





/**
 * [ImageView] extension function adds the capability to loading image by directly specifying
 * the url
 * @param url Image URL
 */


/**
 * Lets the UI act on a controlled bound of states that can be defined here
 * @author Prasan
 * @since 1.0
 */
sealed class ViewState<out T : Any> {

    /**
     * Represents UI state where the UI should be showing a loading UX to the user
     * @param isLoading will be true when the loading UX needs to display, false when not
     */
    data class Loading(val isLoading: Boolean) : ViewState<Nothing>()

    /**
     * Represents the UI state where the operation requested by the UI has been completed successfully
     * and the output of type [T] as asked by the UI has been provided to it
     * @param output result object of [T] type representing the fruit of the successful operation
     */
    data class RenderSuccess<out T : Any>(val output: T) : ViewState<T>()

    /**
     * Represents the UI state where the operation requested by the UI has failed to complete
     * either due to a IO issue or a service exception and the same is conveyed back to the UI
     * to be shown the user
     * @param throwable [Throwable] instance containing the root cause of the failure in a [String]
     */
    data class RenderFailure(val throwable: Throwable) : ViewState<Nothing>()
}

/**
 * Extension function on a fragment to show a toast message
 */
fun Context.showToast(@NonNull message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Extension function on a [Photo] class that will convert the camera data into a single
 * string to be shown on the details screen
 */

/**
 * Returns how long back does the created at date of the [Photo] object go
 * @since 1.0
 */


/**
 * Util method that takes a suspend function returning a [Flow] of [IOTaskResult] as input param and returns a
 * [Flow] of [ViewState], which emits [ViewState.Loading] with true prior to performing the IO Task. If the
 * IO operation results a [IOTaskResult.OnSuccess], the result is mapped to a [ViewState.RenderSuccess] instance and emitted,
 * else a [IOTaskResult.OnFailed] is mapped to a [ViewState.RenderFailure] instance and emitted.
 * The flowable is then completed by emitting a [ViewState.Loading] with false
 */
@ExperimentalCoroutinesApi
suspend fun <T : Any> getViewStateFlowForNetworkCall(ioOperation: suspend () -> Flow<IOTaskResult<T>>) =
    flow {
        emit(ViewState.Loading(true))
        ioOperation().map {
            when (it) {
                is IOTaskResult.OnSuccess -> ViewState.RenderSuccess(it.data)
                is IOTaskResult.OnFailed -> ViewState.RenderFailure(it.throwable)
            }
        }.collect {
            emit(it)
        }
        emit(ViewState.Loading(false))
    }.flowOn(Dispatchers.IO)


/**
 * Show log
 */
    fun showLog(logTag:String,logMsg:String){
        showLog(Log.VERBOSE,logTag,logMsg)
    }

/**
 * Show log type
 */
    fun showLog(logType: Int, logTag: String?, logMsg: String?) {
        if (!BuildConfig.DEBUG) {
            return
        }
        when (logType) {
            Log.VERBOSE, Log.ASSERT -> Log.v(logTag, logMsg!!)
            Log.DEBUG -> Log.d(logTag, logMsg!!)
            Log.ERROR -> Log.e(logTag, logMsg!!)
            Log.INFO -> Log.i(logTag, logMsg!!)
            Log.WARN -> Log.w(logTag, logMsg!!)
            else -> Log.v(logTag, logMsg!!)
        }
    }

/**
 * Get the local address
 */
fun getLocalIpAddress(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    return inetAddress.hostAddress.toString()
                }
            }
        }
    } catch (ex: Exception) {
        Log.e("IP Address", ex.toString())
    }
    return null
}

fun getAppVersionName(context: Context): String? {
    return try {
        context.packageManager.getPackageInfo(getAppPackageName(context)!!, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "1.0"
    }
}

fun getAppPackageName(context: Context): String? {
    return try {
        context.packageName
    } catch (e: java.lang.Exception) {
        "com.matrimonymandaps.vendor"
    }
}


fun getAppVersionCode(context: Context): Int {
    val versionCode: Int?
    val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pInfo.longVersionCode.toInt() // avoid huge version numbers and you will be ok
    } else {
        pInfo.versionCode
    }
    return versionCode
}

fun handleRetrofitFailure(view: View?,errorMessage: String) {
    //var errorMessage: String = Constants.ERROR_SOMETHING_WENT_WRONG
    onSNACK(view!!, errorMessage, "OK")


}

fun showSnackBar(view: View, message: String) = Snackbar
        .make(view, message, Snackbar.LENGTH_SHORT)
        .apply { show() }

fun onSNACK(view: View,errorMessage:String, action:String){
    //Snackbar(view)
    val snackbar = Snackbar.make(view, errorMessage,
            Snackbar.LENGTH_LONG).setAction(action, null)
    snackbar.setActionTextColor(Color.WHITE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.BLACK)
    val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.WHITE)
    textView.textSize = 14f
    snackbar.show()
}

fun isAnyStringNullOrEmpty(vararg strings: String?): Boolean {
    for (s in strings) if (s == null || s.isEmpty()) return true
    return false
}

fun getAndroidID(context: Context): String? {
    return try {
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    } catch (e: java.lang.Exception) {
        "ExceptionCase-AndroidId"
    }
}



/**
 * Go to playstore
 */
fun goToAppPlaystore(context: Context) {
    val uri = Uri.parse("market://details?id=" + getAppPackageName(context))
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
    }
    try {
        context.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + getAppPackageName(context))))
    }
}