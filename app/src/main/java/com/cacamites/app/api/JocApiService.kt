package com.cacamites.app.api

import com.cacamites.app.model.api.LlegendaResponse
import com.cacamites.app.model.api.ResultatJoc
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface JocApiService {
    
    /**
     * Obté les llegendes properes segons la posició GPS de l'usuari.
     * Ruta: /api/llegendes/proximitat
     */
    @GET("api/llegendes/proximitat")
    suspend fun getLlegendesProperes(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("radi") radi: Int = 50
    ): List<LlegendaResponse>

    /**
     * Envia la puntuació final d'un joc al servidor.
     * Ruta: /api/joc/finalitzar
     */
    @POST("api/joc/finalitzar")
    suspend fun enviarPuntuacio(
        @Body resultat: ResultatJoc
    ): Response<Unit>

    /**
     * Obté el rànquing TOP 10 des de Redis.
     */
    @GET("api/ranking/top")
    suspend fun getTopRanking(): List<Map<String, Any>>
}

