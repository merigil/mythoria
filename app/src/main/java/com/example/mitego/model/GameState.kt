package com.example.mitego.model

enum class GameStatus {
    WAITING_TO_START, // Looking for first point
    ACTIVE_PLAY,      // Exploring 7+ points
    BARO_CHALLENGE,   // Timer active
    WON,
    LOST
}

data class GameState(
    val status: GameStatus = GameStatus.WAITING_TO_START,
    val totalScore: Int = 0,
    val inventory: Set<String> = emptySet(), // IDs of items (e.g., "i_sword")
    val timerSecondsRemaining: Long? = null,
    val visitedPoints: Set<String> = emptySet()
)
