package com.example.mitego.model

import org.osmdroid.util.GeoPoint

enum class PointType {
    MANDATORY,
    NARRATIVE,
    OBJECT,
    ENEMY,
    SURPRISE,
    UNKNOWN
}

enum class PointState {
    LOCKED,     // No visible
    VISIBLE,    // Visible al mapa però contingut ocult (misteri)
    COMPLETED   // Desbloquejat (fitxa vista i punts sumats)
}

data class Point(
    val id: String,
    val title: String,
    val coordinate: GeoPoint,
    val type: PointType,
    var state: PointState = PointState.LOCKED,
    val interactionRadius: Double = 12.0, // 10m + marge de precisió GPS
    val warningRadius: Double = 25.0,
    val score: Int = 0,
    val isMandatory: Boolean = false,
    val isTrap: Boolean = false
)
