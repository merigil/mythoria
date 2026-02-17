package com.example.mitego.logic

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitego.model.GameStatus
import com.example.mitego.model.PointState
import com.example.mitego.repository.GameRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameViewModel(
    private val locationManager: LocationManager,
    private val vibrationManager: VibrationManager
) : ViewModel() {
    
    val repository = GameRepository()

    init {
        Log.d("MiteGoDebug", "GameViewModel Initialized")
        startLocationTracking()
    }

    private fun startLocationTracking() {
        viewModelScope.launch {
            locationManager.getLocationFlow().collectLatest { location ->
                checkProximity(location)
            }
        }
    }

    private fun checkProximity(userLocation: Location) {
        val currentPoints = repository.points.value
        val gameStatus = repository.gameState.value.status
        
        currentPoints.forEach { point ->
            // Si el punt ja està completat, no fem res
            if (point.state == PointState.COMPLETED) return@forEach

            // REGLA CRÍTICA: No processem punts de joc si no s'ha activat el Punt d'Inici
            // L'única excepció és el propi "p_start"
            if (point.id != "p_start" && gameStatus == GameStatus.WAITING_TO_START) return@forEach

            val distance = ProximityEngine.calculateDistance(
                userLocation,
                point.coordinate
            )
            
            val proximityStatus = ProximityEngine.checkProximity(distance, point)
            
            when (proximityStatus) {
                ProximityStatus.INTERACTABLE -> {
                    // ACTIVA EL PUNT: Suma/resta punts i permet obrir la fitxa
                    Log.d("MiteGoDebug", "Point Activated by GPS: ${point.id}")
                    repository.onPointVisited(point.id)
                    vibrationManager.vibratePattern() // Vibració forta en entrar al radi d'interacció
                }
                ProximityStatus.WARNING -> {
                    // REVELA EL PUNT: El fa aparèixer al mapa però encara no es pot clicar/activar
                    if (point.state == PointState.LOCKED) {
                        Log.d("MiteGoDebug", "Point Revealed on Map: ${point.id}")
                        repository.updatePointState(point.id, PointState.VISIBLE)
                        vibrationManager.vibrateShort() // Vibració curta en entrar al radi d'avís
                    }
                }
                ProximityStatus.FAR -> {
                    // Si t'allunyes d'un punt que no has completat, el tornem a ocultar? 
                    // Per ara el deixem com està (si s'ha revelat, es queda visible)
                }
            }
        }
    }
}
