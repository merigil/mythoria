package com.cacamites.app.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Interceptor de seguretat per a CaçaMites.
 * Genera una signatura HMAC-SHA256 per a les peticions de finalització de joc.
 */
class SecurityInterceptor(private val secretKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Només signem les peticions de finalitzar joc per optimitzar el rendiment
        if (!originalRequest.url.encodedPath.contains("/joc/finalitzar")) {
            return chain.proceed(originalRequest)
        }

        val timestamp = System.currentTimeMillis().toString()
        // La puntuació s'ha d'incloure en el càlcul del hash. 
        // Es pot obtenir del body o d'un query param depenent de la implementació.
        val score = originalRequest.url.queryParameter("score") ?: "0"
        
        // Generació de la signatura HMAC-SHA256
        val signature = generateHmacSignature("$score:$timestamp", secretKey)

        val newRequest = originalRequest.newBuilder()
            .header("X-Timestamp", timestamp)
            .header("X-Signature", signature)
            .header("X-App-Version", "1.0.0")
            .build()

        return chain.proceed(newRequest)
    }

    private fun generateHmacSignature(data: String, key: String): String {
        return try {
            val sha256Hmac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(key.toByteArray(), "HmacSHA256")
            sha256Hmac.init(secretKeySpec)
            sha256Hmac.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }
}
