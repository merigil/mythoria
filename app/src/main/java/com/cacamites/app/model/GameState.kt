package com.cacamites.app.model

enum class GameStatus {
    WAITING_TO_START,
    ACTIVE_PLAY,
<<<<<<< HEAD
=======
    BARO_CHALLENGE,
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
    WON,
    LOST
}

data class GameState(
    val totalScore: Int = 0,
    val inventory: Set<String> = emptySet(),
    val visitedPoints: Set<String> = emptySet(),
    val status: GameStatus = GameStatus.WAITING_TO_START,
<<<<<<< HEAD
    val timerSecondsRemaining: Long? = null,
    val isGameActive: Boolean = false,
    val currentScore: Int = 0,
    val keyItemsCollected: List<String> = emptyList(),
    val isTimerActive: Boolean = false,
    val timeRemainingMs: Long = 0
=======
    val timerSecondsRemaining: Long? = null
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
)

