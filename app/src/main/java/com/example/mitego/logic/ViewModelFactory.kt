package com.example.mitego.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val locationManager: LocationManager,
    private val vibrationManager: VibrationManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(locationManager, vibrationManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
