package com.cacamites.app.model.api

data class LlegendaResponse(
    val id: String,
    val titol: String,
    val lat: Double,
    val lng: Double,
    val dificultat: String,
    val icona_url: String? = null
)

data class ResultatJoc(
    val jugador_id: String,
    val llegenda_id: Int,
    val puntuacio: Int,
    val timestamp: Long
)
