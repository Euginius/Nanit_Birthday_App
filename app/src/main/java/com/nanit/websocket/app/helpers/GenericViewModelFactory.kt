package com.nanit.websocket.app.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GenericViewModelFactory<T : ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(creator().javaClass)) {
            return creator() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}