package com.mvl.mvl_assignment.ui.viewmodel


import androidx.annotation.NonNull
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel


open class BaseViewModel : ViewModel(), Observable {
    @Transient
    private var mCallbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(@NonNull callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(@NonNull callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks.remove(callback)
    }


}
