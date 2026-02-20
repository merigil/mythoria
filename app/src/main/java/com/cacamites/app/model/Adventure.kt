package com.cacamites.app.model

data class Adventure(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String, // For the dashboard card background or icon
    val points: List<Point>,
    val cards: List<Card>,
    val isActive: Boolean = true // Whether it is playable
)

