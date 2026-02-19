package com.cacamites.app.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Client de Retrofit per gestionar la comunicació amb l'API de CaçaMites.
 */
object RetrofitClient {
    
    // TODO: Canviar per la URL definitiva quan es desplegui a Railway
    private const val BASE_URL = "https://cacamites-api.up.railway.app/"
    
    // TODO: La clau secreta hauria de venir d'un lloc segur (ex: BuildConfig o Secrets)
    private const val HMAC_SECRET = "LA_TEVA_CLAU_SECRETA_QUE_NOMA_S_SAPS_TU"

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(SecurityInterceptor(HMAC_SECRET))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: JocApiService by lazy {
        retrofit.create(JocApiService::class.java)
    }
}
