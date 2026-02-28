package com.cacamites.app.repository

import android.location.Location
import com.cacamites.app.model.Card
import com.cacamites.app.model.GameState
import com.cacamites.app.model.GameStatus
import com.cacamites.app.model.Point
import com.cacamites.app.model.PointState
import com.cacamites.app.model.PointType
import com.cacamites.app.model.Quiz
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

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation: StateFlow<GeoPoint?> = _userLocation.asStateFlow()

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
        _gameState.update { it.copy(legendId = type, status = GameStatus.WAITING_TO_START) }
    }

    private fun createPoints(): List<Point> {
        return listOf(
            // --- SAVASSONA ---
            Point("p_start", "Punt d'Inici", GeoPoint(41.931992, 2.252433), PointType.NARRATIVE, PointState.VISIBLE, score = 0),
            Point("p_llops1", "Llops Punt 01", GeoPoint(41.931965, 2.251870), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_llops2", "Llops Punt 02", GeoPoint(41.931997, 2.252057), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_llops3", "Llops Punt 03", GeoPoint(41.932001, 2.252188), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_soldat", "Soldat", GeoPoint(41.931959, 2.252261), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_baronessa", "La Baronessa i el Jove", GeoPoint(41.931551, 2.251217), PointType.MANDATORY, score = 20, isMandatory = true),
            Point("p_espasa", "L'Espasa Màgica", GeoPoint(41.932000, 2.252444), PointType.OBJECT, score = 30, isMandatory = true), 
            Point("p_pastora", "La Pastora", GeoPoint(41.931391, 2.251742), PointType.NARRATIVE, score = 10),
            Point("p_mercader", "Mercader Ambulant", GeoPoint(41.931339, 2.251871), PointType.OBJECT, score = 15),
            Point("p_ancia", "L'Ancià dels mapes", GeoPoint(41.931764, 2.251324), PointType.OBJECT, score = 5),
            Point("p_bassa", "La bassa i el follet", GeoPoint(41.931638, 2.251472), PointType.OBJECT, score = 15),
            Point("p_prat", "Un prat", GeoPoint(41.931674, 2.251708), PointType.NARRATIVE, score = 15),
            Point("p_clerge", "El Clerga", GeoPoint(41.932167, 2.252639), PointType.QUIZ, score = 0, quiz = Quiz(
                question = "Quin és l’estil amb què està construïda aquesta església?",
                options = listOf("Clàssic", "Romànic Llombard", "Gòtic"),
                correctOptionIndex = 1,
                pointsIfCorrect = 15
            )),
            Point("p_pastor", "El Pastor", GeoPoint(41.931659, 2.252168), PointType.ENEMY, score = -10, isTrap = true),
            Point("p_nen", "Nen Trepella", GeoPoint(41.931396, 2.252176), PointType.ENEMY, score = -5, isTrap = true),
            Point("p_bota", "Bota de Vi", GeoPoint(41.931224, 2.251789), PointType.ENEMY, score = -8, isTrap = true),
            Point("p_baro", "El Baró", GeoPoint(41.931540, 2.251650), PointType.ENEMY, score = 0, isMandatory = true, state = PointState.LOCKED),
            Point("p_font_baronessa", "La font de la Baronessa", GeoPoint(41.931559, 2.251943), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            
            // --- MANLLEU ---
            Point("s_start", "Punt d'Inici", GeoPoint(41.932250, 2.252556), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_pregoner", "El Pregoner", GeoPoint(41.930508, 2.253943), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_barquer", "La barquera", GeoPoint(41.930191, 2.253875), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_vaquer", "El vaquer", GeoPoint(41.930153, 2.254478), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_rubi", "El diamant de la serpent", GeoPoint(41.930557, 2.254912), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = true),
            Point("s_morter", "El Morter", GeoPoint(41.930529, 2.254812), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = true),
            Point("s_final", "La plaça", GeoPoint(41.930523, 2.254483), PointType.NARRATIVE, PointState.LOCKED, score = 0)
        )
    }

    private fun createCards(): List<Card> {
        return listOf(
            Card("c_p_start", "Punt d'Inici", "Benvingut, noble explorador. Sóc la Isolda, la trobadora. Avui recorreràs les terres del Baró per protegir un amor prohibit.", "Inici", "la_trobadora_02", true),
            Card("c_p_espasa", "L'Espasa Màgica", "Aquesta vella espasa guarda el poder sagrat de defensar els enamorats.", "Objecte", "la_espassa", true),
            Card("c_s_rubi", "El diamant de la serpent", "Heu trobat el diamant encantat! La serpent us persegueix!", "Objecte", "serpent_diamant_serpent", true),
            Card("c_s_morter", "El Morter", "Heu trobat el morter! Correu a la plaça!", "Objecte", "serpent_morter", true)
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
        
        if (pointId == "p_start") unlockInitialPoints()
        if (pointId == "p_baronessa") updatePointState("p_baro", PointState.VISIBLE)
        if (pointId == "p_baro") startBaroTimer()
        if (pointId == "s_vaquer") updatePointState("s_rubi", PointState.VISIBLE)
        if (pointId == "s_rubi") startSerpentTimer()
    }

    private fun unlockInitialPoints() {
        _points.update { list ->
            list.map { 
                if (it.state == PointState.LOCKED && !it.id.startsWith("s_") && it.id != "p_baro") it.copy(state = PointState.VISIBLE) else it
            }
        }
    }

    fun onQuizAnswered(pointId: String, selectedIndex: Int) {
        val point = _points.value.find { it.id == pointId } ?: return
        val quiz = point.quiz ?: return
        if (point.state == PointState.COMPLETED) return
        val isCorrect = selectedIndex == quiz.correctOptionIndex
        _gameState.update { it.copy(totalScore = it.totalScore + (if (isCorrect) quiz.pointsIfCorrect else 0)) }
        updatePointState(pointId, PointState.COMPLETED)
    }

    private fun startBaroTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = BARO_TIMER_SECONDS, status = GameStatus.ACTIVE_PLAY) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { it.copy(timerSecondsRemaining = (it.timerSecondsRemaining ?: 0) - 1) }
            }
            _gameState.update { it.copy(status = GameStatus.LOST) }
        }
    }

    private fun startSerpentTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = SERPENT_TIMER_SECONDS, status = GameStatus.ACTIVE_PLAY) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { it.copy(timerSecondsRemaining = (it.timerSecondsRemaining ?: 0) - 1) }
            }
            _gameState.update { it.copy(status = GameStatus.LOST) }
        }
    }

    fun updateUserLocation(lat: Double, lng: Double) {
        _userLocation.value = GeoPoint(lat, lng)
    }

    fun checkProximity(userLat: Double, userLng: Double, targetLat: Double, targetLng: Double, threshold: Float = 20f): Pair<Boolean, Float> {
        val userLoc = Location("user").apply { latitude = userLat; longitude = userLng }
        val targetLoc = Location("target").apply { latitude = targetLat; longitude = targetLng }
        val distance = userLoc.distanceTo(targetLoc)
        return Pair(distance <= threshold, distance)
    }
}
