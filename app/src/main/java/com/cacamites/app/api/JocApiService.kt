package com.cacamites.app.api

import com.cacamites.app.model.api.LlegendaResponse
import com.cacamites.app.model.api.ResultatJoc
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface JocApiService {
    
    /**
     * Obté les llegendes properes segons la posició GPS de l'usuari.
     */
    @GET("map/llegendes")
    suspend fun getLlegendesProperes(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radi") radi: Int = 500
    ): List<LlegendaResponse>

    /**
     * Envia la puntuació final d'un joc al servidor.
     * Inclou la signatura HMAC a la capçalera X-Signature per seguretat.
     */
    @POST("joc/finalitzar")
    suspend fun enviarPuntuacio(
        @Header("X-Signature") signature: String,
        @Body resultat: ResultatJoc
    ): Response<Unit>
}
