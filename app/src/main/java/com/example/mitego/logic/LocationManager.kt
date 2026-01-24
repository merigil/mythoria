package com.example.mitego.logic

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationManager(private val context: Context) {

    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLocationFlow(): Flow<Location> = callbackFlow {
        Log.d("MiteGoDebug", "Requesting Location Updates (1s interval)")
        
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000) // 1 segon
            .setMinUpdateDistanceMeters(0f) // Qualsevol moviment
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { 
                    Log.d("MiteGoDebug", "GPS Location: ${it.latitude}, ${it.longitude}")
                    trySend(it) 
                }
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose {
            Log.d("MiteGoDebug", "Stopping Location Updates")
            client.removeLocationUpdates(callback)
        }
    }
}
