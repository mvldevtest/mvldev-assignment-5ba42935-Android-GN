package com.mvl.mvl_assignment.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

inline fun <reified VM : ViewModel> Fragment.factory(crossinline provider: () -> Provider<VM>): () -> ViewModelProvider.Factory {
    return {
        val viewModelProvider = runCatching(provider).getOrNull()
        if (viewModelProvider == null) defaultViewModelProviderFactory
        else object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                if (modelClass.isAssignableFrom(VM::class.java)) return viewModelProvider.get() as T
                else error("Cannot create ${modelClass.simpleName} with a Provider of ${VM::class.java.simpleName}")
            }
        }
    }
}