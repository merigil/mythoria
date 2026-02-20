package com.example.mitego.model

import org.osmdroid.util.GeoPoint

enum class PointType {
    MANDATORY,
    NARRATIVE,
    OBJECT,
    HIDDEN_OBJECT,
    QUIZ,
    ENEMY,
    SURPRISE
}

enum class PointState {
    LOCKED,
    VISIBLE,
    COMPLETED
}

data class Quiz(
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val pointsIfCorrect: Int
)

data class Point(
    val id: String,
    val title: String,
    val coordinate: GeoPoint,
    val type: PointType,
    var state: PointState = PointState.LOCKED,
    val interactionRadius: Double = 10.0,
    val warningRadius: Double = 20.0,
    val score: Int = 0,
    val isMandatory: Boolean = false,
    val isTrap: Boolean = false,
    val isAlwaysInvisible: Boolean = false,
    val quiz: Quiz? = null
)
