package com.example.mitego.model

enum class GameResult {
    WIN,
    DEFEAT_TIME,
    DEFEAT_SCORE
}

data class GameState(
    val currentScore: Int = 0,
    val keyItemsCollected: List<String> = emptyList(), // IDs of collected key items
    val isGameActive: Boolean = false,
    val timeRemainingMs: Long = 15 * 60 * 1000L, // 15 minutes
    val gameResult: GameResult? = null,
    val isTimerActive: Boolean = false
) {
    val scoreTarget = 101 // Must be > 100
    
    fun hasWon(): Boolean {
        // IDs for Jove, Baronessa, Espasa
        val requiredItems = listOf("p_jove", "p_baronessa", "p_espasa") 
        return currentScore >= scoreTarget && keyItemsCollected.containsAll(requiredItems)
    }
}
