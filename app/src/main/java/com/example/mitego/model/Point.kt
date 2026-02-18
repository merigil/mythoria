package com.example.mitego.model

import org.osmdroid.util.GeoPoint

enum class PointType {
    MANDATORY,
    NARRATIVE,
    OBJECT,
    ENEMY,
    SURPRISE
}

enum class PointState {
    LOCKED,
    VISIBLE,
    COMPLETED
}

data class Point(
    val id: String,
    val title: String,
    val coordinate: GeoPoint,
    val type: PointType,
    var state: PointState = PointState.LOCKED,
    val interactionRadius: Double = 10.0,
    val warningRadius: Double = 20.0,
    val scoreValue: Int = 0,
    val isKeyItem: Boolean = false
)
