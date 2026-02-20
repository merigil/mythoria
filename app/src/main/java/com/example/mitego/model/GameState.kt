package com.example.mitego.model

enum class GameStatus {
    WAITING_TO_START,
    ACTIVE_PLAY,
    BARO_CHALLENGE,
    WON,
    LOST
}

data class GameState(
    val totalScore: Int = 0,
    val inventory: Set<String> = emptySet(),
    val visitedPoints: Set<String> = emptySet(),
    val status: GameStatus = GameStatus.WAITING_TO_START,
    val timerSecondsRemaining: Long? = null
)
