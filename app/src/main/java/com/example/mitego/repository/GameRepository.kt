package com.example.mitego.repository

import android.location.Location
import com.example.mitego.model.Card
import com.example.mitego.model.GameState
import com.example.mitego.model.GameStatus
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.model.PointType
import com.example.mitego.model.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class GameRepository {

    private val BARO_TIMER_SECONDS = 15 * 60L 
    private val SERPENT_TIMER_SECONDS = 30 * 60L
    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation: StateFlow<GeoPoint?> = _userLocation.asStateFlow()

    private val _isUserMock = MutableStateFlow(false)
    val isUserMock: StateFlow<Boolean> = _isUserMock.asStateFlow()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    init {
        initializeGame()
    }

    private fun initializeGame() {
        _cards.value = createCards()
        _points.value = createPoints().filter { it.id.startsWith("p_") || it.id == "p_start" }
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }

    fun startNewLegend(type: String) {
        timerJob?.cancel()
        val allPoints = createPoints()
        val filteredPoints = when(type) {
            "SERPENT" -> allPoints.filter { it.id.startsWith("s_") }
            else -> allPoints.filter { it.id.startsWith("p_") }
        }
        
        val initialPoints = filteredPoints.map { 
            if (it.id.endsWith("_start")) it.copy(state = PointState.VISIBLE) else it 
        }
        
        _points.value = initialPoints
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }

    private fun createPoints(): List<Point> {
        return listOf(
            // SAVASSONA
            Point("p_start", "Punt d'Inici", GeoPoint(41.956361, 2.340167), PointType.NARRATIVE, PointState.VISIBLE, score = 0),
            Point("p_baronessa", "La Baronessa", GeoPoint(41.953683, 2.335898), PointType.MANDATORY, score = 12, isMandatory = true),
            Point("p_jove", "El Jove criat", GeoPoint(41.9538348, 2.336077), PointType.MANDATORY, score = 12, isMandatory = true),
            Point("p_espassa", "L'Espassa Màgica", GeoPoint(41.954496, 2.336380), PointType.OBJECT, score = 30, isMandatory = true), 
            Point("p_baro", "El Baró", GeoPoint(41.953403, 2.336365), PointType.ENEMY, score = 0, isMandatory = true, state = PointState.LOCKED),
            
            // MANLLEU
            Point("s_start", "Plaça de Fra Bernadí", GeoPoint(41.930675, 2.254059), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_vaquer", "El vaquer", GeoPoint(41.930153, 2.254478), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_rubi", "El rubí de la serpent", GeoPoint(41.930170, 2.254787), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = false),
            Point("s_morter", "El Morter", GeoPoint(41.930555, 2.254905), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = false),
            Point("s_final", "La plaça", GeoPoint(41.930523, 2.254483), PointType.NARRATIVE, PointState.LOCKED, score = 0)
        )
    }

    private fun createCards(): List<Card> {
        return listOf(
            Card("c_s_rubi", "El rubí de la serpent", "Has trobat el rubí!", "Objecte", "el_rubi", true),
            Card("c_s_morter", "El Morter", "Has trobat el morter!", "Objecte", "serpent_morter", true)
        )
    }

    fun getCard(cardId: String): Card {
        return _cards.value.find { it.id == cardId } 
            ?: Card(cardId, "Indret Desconegut", "Has trobat un lloc misteriós...", "Misteri", null, true)
    }

    fun updatePointState(pointId: String, newState: PointState) {
        _points.update { list ->
            list.map { if (it.id == pointId) it.copy(state = newState) else it }
        }
    }

    fun onPointVisited(pointId: String) {
        val point = _points.value.find { it.id == pointId } ?: return
        if (point.state == PointState.COMPLETED) return
        
        updatePointState(pointId, PointState.COMPLETED)
        
        _gameState.update { state ->
            val newInventory = state.inventory.toMutableSet().apply { 
                if (point.type == PointType.HIDDEN_OBJECT || point.type == PointType.OBJECT) add(pointId)
            }
            state.copy(
                totalScore = state.totalScore + point.score,
                inventory = newInventory,
                visitedPoints = state.visitedPoints + pointId
            )
        }
    }

    fun updateUserLocation(lat: Double, lng: Double, isMock: Boolean = false) {
        _userLocation.value = GeoPoint(lat, lng)
        _isUserMock.value = isMock
    }

    fun checkProximity(userLat: Double, userLng: Double, targetLat: Double, targetLng: Double, threshold: Float = 50f): Pair<Boolean, Float> {
        val userLoc = Location("user").apply { latitude = userLat; longitude = userLng }
        val targetLoc = Location("target").apply { latitude = targetLat; longitude = targetLng }
        val distance = userLoc.distanceTo(targetLoc)
        return Pair(distance <= threshold, distance)
    }
}
