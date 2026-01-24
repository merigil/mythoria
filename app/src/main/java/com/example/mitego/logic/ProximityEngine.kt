package com.example.mitego.logic

import android.location.Location
import com.example.mitego.model.Point
import org.osmdroid.util.GeoPoint

object ProximityEngine {

    fun calculateDistance(userLoc: Location, pointLoc: GeoPoint): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            userLoc.latitude, userLoc.longitude,
            pointLoc.latitude, pointLoc.longitude,
            results
        )
        return results[0]
    }

    // Determine state based on distance
    // Returns: Null if no change, or new interaction status
    fun checkProximity(distance: Float, point: Point): ProximityStatus {
        return when {
            distance <= point.interactionRadius -> ProximityStatus.INTERACTABLE
            distance <= point.warningRadius -> ProximityStatus.WARNING
            else -> ProximityStatus.FAR
        }
    }
}

enum class ProximityStatus {
    FAR,
    WARNING,
    INTERACTABLE
}
