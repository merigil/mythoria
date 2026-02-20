package com.cacamites.app.model

data class Card(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val imageUrl: String? = null,
    val isUnlocked: Boolean = false
)

