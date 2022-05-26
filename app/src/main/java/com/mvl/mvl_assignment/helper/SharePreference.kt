package com.mvl.mvl_assignment.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import java.util.*
import kotlin.reflect.KProperty


abstract class PrefDelegate<T>(val prefName: String?, val prefKey: String) {

    companion object {
        private var context: Context? = null



        /**
         * Initialize PrefDelegate with a Context reference
         * !! This method needs to be called before any other usage of PrefDelegate !!
         */
        fun init(context: Context) {
            Companion.context = context
        }


    }



    protected val prefs: SharedPreferences by lazy {
        if (context != null)
            if (prefName != null) context!!.getSharedPreferences(prefName, Context.MODE_PRIVATE) else PreferenceManager.getDefaultSharedPreferences(
                context!!)
        else
            throw IllegalStateException("Context was not initialized. Call PrefDelegate.init(context) before using it")
    }


    protected fun clear() {
        prefs.edit().clear().apply()
    }




    abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)



}





fun stringPref(prefKey: String, defaultValue: String? = null) = StringPrefDelegate(null, prefKey, defaultValue)

fun stringPref(prefName: String, prefKey: String, defaultValue: String? = null) = StringPrefDelegate(prefName, prefKey, defaultValue)


class StringPrefDelegate(prefName: String?, prefKey: String, val defaultValue: String?) : PrefDelegate<String?>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getString(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) = prefs.edit().putString(prefKey, value).apply()
}

class clearPrefDelegate() : PrefDelegate<String?>(null, "null") {
    override fun getValue(thisRef: Any?, property: KProperty<*>) =null
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) = prefs.edit().clear().apply()

}


fun intPref(prefKey: String, defaultValue: Int = 0) = IntPrefDelegate(null, prefKey, defaultValue)
fun intPref(prefName: String, prefKey: String, defaultValue: Int = 0) = IntPrefDelegate(prefName, prefKey, defaultValue)
class IntPrefDelegate(prefName: String?, prefKey: String, val defaultValue: Int) : PrefDelegate<Int>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getInt(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = prefs.edit().putInt(prefKey, value).apply()
    operator fun setValue(sharePrefStorage: SharePrefStorage, property: KProperty<*>, b: Int?) =prefs.edit().putInt(prefKey, b!!).apply()


}


fun floatPref(prefKey: String, defaultValue: Float = 0f) = FloatPrefDelegate(null, prefKey, defaultValue)
fun floatPref(prefName: String, prefKey: String, defaultValue: Float = 0f) = FloatPrefDelegate(prefName, prefKey, defaultValue)
class FloatPrefDelegate(prefName: String?, prefKey: String, val defaultValue: Float) : PrefDelegate<Float>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getFloat(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) = prefs.edit().putFloat(prefKey, value).apply()


}

fun booleanPref(prefKey: String, defaultValue: Boolean = false) = BooleanPrefDelegate(null, prefKey, defaultValue)
fun booleanPref(prefName: String, prefKey: String, defaultValue: Boolean = false) = BooleanPrefDelegate(prefName, prefKey, defaultValue)
class BooleanPrefDelegate(prefName: String?, prefKey: String, val defaultValue: Boolean) : PrefDelegate<Boolean>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getBoolean(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = prefs.edit().putBoolean(prefKey, value).apply()
    operator fun setValue(sharePrefStorage: SharePrefStorage, property: KProperty<*>, b: Boolean?) =prefs.edit().putBoolean(prefKey, b!!).apply()


}



fun longPref(prefKey: String, defaultValue: Long = 0L) = LongPrefDelegate(null, prefKey, defaultValue)
fun longPref(prefName: String, prefKey: String, defaultValue: Long = 0L) = LongPrefDelegate(prefName, prefKey, defaultValue)
class LongPrefDelegate(prefName: String?, prefKey: String, val defaultValue: Long) : PrefDelegate<Long>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getLong(prefKey, defaultValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) = prefs.edit().putLong(prefKey, value).apply()

    fun stringSetPref(prefKey: String, defaultValue: Set<String> = HashSet<String>()) = StringSetPrefDelegate(null, prefKey, defaultValue)
    fun stringSetPref(prefName: String, prefKey: String, defaultValue: Set<String> = HashSet<String>()) = StringSetPrefDelegate(prefName, prefKey, defaultValue)
    class StringSetPrefDelegate(prefName: String?, prefKey: String, val defaultValue: Set<String>) : PrefDelegate<Set<String>>(prefName, prefKey) {


        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>) = prefs.edit().putStringSet(prefKey, value).apply()
        override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String> {
            TODO("implement later")
        }


        //override fun getValue(thisRef: Any?, property: KProperty<*>, value: Set<String>)= prefs.getStringSet(prefKey, defaultValue)
    }

}





