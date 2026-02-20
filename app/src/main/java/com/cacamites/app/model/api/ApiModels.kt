package com.cacamites.app.model.api

/**
 * Resposta del servidor quan es consulta la proximitat de llegendes.
 */
data class LlegendaResponse(
    val id: Int,
    val titol: String,
    val distancia_metres: Double,
    val activa: Boolean,
    val config: Map<String, Any>? = null
)

/**
 * Model per enviar el resultat d'una partida al servidor.
 * Inclou coordenades per a la re-validació de geofencing al backend.
 */
data class ResultatJoc(
    val jugador_id: String,
    val llegenda_id: Int,
    val puntuacio: Int,
    val lat: Double,
    val lon: Double,
    val timestamp: Long
)
