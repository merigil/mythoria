package com.cacamites.app.model

enum class GameStatus {
    WAITING_TO_START,
    ACTIVE_PLAY,
    BARO_CHALLENGE,
    WON,
    LOST
}

data class GameState(
    val legendId: String? = null,
    val totalScore: Int = 0,
    val inventory: Set<String> = emptySet(),
    val visitedPoints: Set<String> = emptySet(),
    val status: GameStatus = GameStatus.WAITING_TO_START,
    val timerSecondsRemaining: Long? = null,
    val isGameActive: Boolean = false,
    val currentScore: Int = 0,
    val keyItemsCollected: List<String> = emptyList(),
    val isTimerActive: Boolean = false,
    val timeRemainingMs: Long = 0,
    val isChased: Boolean = false
)
