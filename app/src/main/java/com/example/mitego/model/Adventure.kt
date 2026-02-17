package com.example.mitego.model

data class Adventure(
    val id: String,
    val title: String,
    val region: String,
    val estimatedTime: String,
    val difficulty: String,
    val isAccessible: Boolean,
    val heroImageUrl: String?,
    val config: AdventureConfig,
    val points: List<Point>,
    val cards: List<Card>
)

data class AdventureConfig(
    val winCondition: WinCondition,
    val challengeTimerSeconds: Long? = null
)

data class WinCondition(
    val minScore: Int = 0,
    val requiredItems: List<String> = emptyList(),
    val requiredPoints: List<String> = emptyList(),
    val finalPointId: String? = null,
    val finalTimerSeconds: Int? = null
)
