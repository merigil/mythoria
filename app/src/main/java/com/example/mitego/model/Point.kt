package com.example.mitego.model

import org.osmdroid.util.GeoPoint

enum class PointType {
    MANDATORY,
    NARRATIVE,
    OBJECT,
    ENEMY,
    SURPRISE,
    UNKNOWN,
    QUIZ,
    HIDDEN_OBJECT // Nou tipus per a objectes invisibles
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
    val isTrap: Boolean = false,
    val quiz: Quiz? = null,
    val isAlwaysInvisible: Boolean = false, // Propietat per forçar invisibilitat al mapa
    val nextPointId: String? = null,        // ID del següent punt a desbloquejar (per a seqüències)
    val triggersTimer: Boolean = false      // Indica si aquest punt activa un cronòmetre
)

data class Quiz(
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val pointsIfCorrect: Int
)
