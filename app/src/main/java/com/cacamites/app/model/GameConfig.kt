package com.cacamites.app.model

import org.osmdroid.util.GeoPoint

object GameConfig {
    const val IS_TEST_MODE = true // Canvia a false per usar coordenades reals

    // Coordenades de prova (a prop de 41.93, 2.25 segons la petició)
    private val TEST_COORDS = mapOf(
        "p_start" to GeoPoint(41.931992, 2.252433),
        "p_llops1" to GeoPoint(41.931965, 2.251870),
        "p_llops2" to GeoPoint(41.931997, 2.252057),
        "p_llops3" to GeoPoint(41.932001, 2.252188),
        "p_soldat" to GeoPoint(41.931959, 2.252261),
        "p_baronessa_jove" to GeoPoint(41.931551, 2.251217),
        "p_espassa" to GeoPoint(41.931533, 2.252467), // 41°55'55.2"N 2°15'08.8"E -> dec: 41.932, 2.252444 aprox
        "p_pastora" to GeoPoint(41.931391, 2.251742),
        "p_mercader" to GeoPoint(41.931339, 2.251871),
        "p_ancia" to GeoPoint(41.931764, 2.251324),
        "p_bassa" to GeoPoint(41.931638, 2.251472),
        "p_prat" to GeoPoint(41.931674, 2.251708),
        "p_clerge" to GeoPoint(41.931690, 2.251900),
        "p_pastor" to GeoPoint(41.931659, 2.252168),
        "p_nen" to GeoPoint(41.931396, 2.252176),
        "p_bota" to GeoPoint(41.931224, 2.251789),
        "p_baro" to GeoPoint(41.931540, 2.251650),
        "p_moneda1" to GeoPoint(41.931071, 2.251945),
        "p_moneda2" to GeoPoint(41.930961, 2.251918),
        "p_moneda3" to GeoPoint(41.930668, 2.251813),
        "p_font_baronessa" to GeoPoint(41.931559, 2.251943)
    )

    // Coordenades reals (Savassona)
    private val REAL_COORDS = mapOf(
        "p_start" to GeoPoint(41.956361, 2.340167),
        "p_baronessa_jove" to GeoPoint(41.953683, 2.335898),
        "p_espassa" to GeoPoint(41.954496, 2.336380),
        "p_pastora" to GeoPoint(41.956444, 2.340611),
        "p_mercader" to GeoPoint(41.954806, 2.336472),
        "p_ancia" to GeoPoint(41.955870, 2.337467),
        "p_bassa" to GeoPoint(41.955057, 2.337857),
        "p_prat" to GeoPoint(41.956118, 2.338699),
        "p_clerge" to GeoPoint(41.956500, 2.338150),
        "p_pastor" to GeoPoint(41.956050, 2.338800),
        "p_nen" to GeoPoint(41.956465, 2.338860),
        "p_bota" to GeoPoint(41.955700, 2.337700),
        "p_baro" to GeoPoint(41.953403, 2.336365),
        "p_moneda1" to GeoPoint(41.953540, 2.335920),
        "p_moneda2" to GeoPoint(41.956579, 2.340790),
        "p_moneda3" to GeoPoint(41.956300, 2.339300),
        "p_font_baronessa" to GeoPoint(41.953800, 2.336105)
    )

    fun getCoord(id: String): GeoPoint {
        return if (IS_TEST_MODE) {
            TEST_COORDS[id] ?: TEST_COORDS["p_start"]!!
        } else {
            REAL_COORDS[id] ?: REAL_COORDS["p_start"]!!
        }
    }
}
