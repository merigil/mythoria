package com.example.mitego.repository

import com.example.mitego.model.Adventure
import com.example.mitego.model.Card
import com.example.mitego.model.GameResult
import com.example.mitego.model.GameState
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.model.PointType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.location.Location
import org.osmdroid.util.GeoPoint
import java.util.Random

class GameRepository {

    private val BARONESSA_CENTER = GeoPoint(41.9323395951151, 2.252533598712291)
    private val SAVASSONA_CENTER = GeoPoint(41.956361, 2.340167)
    private val MANLLEU_CENTER = GeoPoint(41.930675, 2.254059)
    
    // Game State
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val gameScope = CoroutineScope(Dispatchers.Main)

    // Adventure Definition
    private val baronessaAdventure by lazy {
        val generatedPoints = generateBaronessaPoints()
        val cards = generateBaronessaCards(generatedPoints)
        
        Adventure(
            id = "adv_baronessa",
            title = "La Font de la Baronessa",
            description = "Descobreix els secrets de la Baronessa...",
            imageUrl = "",
            points = generatedPoints,
            cards = cards,
            isActive = true
        )
    }

    private val serpentAdventure by lazy {
        val generatedPoints = generateSerpentPoints()
        val cards = generateSerpentCards(generatedPoints)
        
        Adventure(
            id = "adv_serpent",
            title = "La Serpent de Manlleu",
            description = "Recupera el rubí de la serpent...",
            imageUrl = "",
            points = generatedPoints,
            cards = cards,
            isActive = true
        )
    }

    private val castellAdventure = Adventure(
        id = "adv_castell",
        title = "El Castell Perdut",
        description = "Explora les ruïnes de l'antic castell...",
        imageUrl = "",
        points = emptyList(),
        cards = emptyList(),
        isActive = false
    )

    val adventures: List<Adventure> = listOf(baronessaAdventure, serpentAdventure, castellAdventure)
    private var currentAdventure: Adventure? = null

    fun selectAdventure(adventureId: String) {
        val adventure = adventures.find { it.id == adventureId }
        if (adventure != null) {
            currentAdventure = adventure
            _points.value = adventure.points
            _cards.value = adventure.cards
            startGame()
        }
    }

    private fun startGame() {
        _gameState.value = GameState(isGameActive = true, currentScore = 0)
    }

    fun collectPoint(pointId: String) {
        val currentPoints = _points.value.toMutableList()
        val pointIndex = currentPoints.indexOfFirst { it.id == pointId }
        
        if (pointIndex != -1) {
            val point = currentPoints[pointIndex]
            if (point.state == PointState.COMPLETED) return

            // Update Point State
            val updatedPoint = point.copy(state = PointState.COMPLETED)
            currentPoints[pointIndex] = updatedPoint
            _points.value = currentPoints

            // Update Game State
            _gameState.update { state ->
                val newScore = state.currentScore + point.scoreValue
                val newKeys = if (point.isKeyItem) state.keyItemsCollected + point.id else state.keyItemsCollected
                
                var newState = state.copy(
                    currentScore = newScore,
                    keyItemsCollected = newKeys
                )

                // Check Baron Logic
                if (point.id == "p_baro") {
                    newState = newState.copy(isTimerActive = true)
                    startTimer()
                }

                newState
            }
            
            // Unlock Card if exists
            unlockCard("c_${pointId.removePrefix("p_").removePrefix("s_")}")
        }
    }

    /**
     * Calcula si el jugador està prou a prop per jugar.
     */
    fun checkProximity(
        userLat: Double, userLng: Double, 
        targetLat: Double, targetLng: Double, 
        threshold: Float = 50f
    ): Pair<Boolean, Float> {
        val userLocation = Location("user").apply {
            latitude = userLat
            longitude = userLng
        }
        val targetLocation = Location("target").apply {
            latitude = targetLat
            longitude = targetLng
        }
        val distance = userLocation.distanceTo(targetLocation)
        return Pair(distance <= threshold, distance)
    }

    fun canCollectPoint(pointId: String, userLat: Double, userLng: Double): Boolean {
        val point = _points.value.find { it.id == pointId } ?: return false
        val (isNear, _) = checkProximity(userLat, userLng, point.coordinate.latitude, point.coordinate.longitude)
        return isNear
    }

    private fun startTimer() {
        gameScope.launch {
            while (_gameState.value.isTimerActive && _gameState.value.timeRemainingMs > 0) {
                delay(1000)
                _gameState.update { 
                    it.copy(timeRemainingMs = it.timeRemainingMs - 1000) 
                }
            }
            if (_gameState.value.isTimerActive) {
                checkEndGameCondition()
            }
        }
    }

    private fun checkEndGameCondition() {
        _gameState.update { state ->
            val hasWon = state.hasWon()
            state.copy(
                isGameActive = false,
                isTimerActive = false,
                gameResult = if (hasWon) GameResult.WIN else GameResult.DEFEAT_TIME
            )
        }
    }

    fun getCard(cardId: String): Card? {
        return _cards.value.find { it.id == cardId }
    }
    
    fun unlockCard(cardId: String) {
        val currentCards = _cards.value.toMutableList()
        val cardIndex = currentCards.indexOfFirst { it.id == cardId || it.id == "c_$cardId" }
        if (cardIndex != -1) {
            currentCards[cardIndex] = currentCards[cardIndex].copy(isUnlocked = true)
            _cards.value = currentCards
        }
    }

    // --- Data Generation ---

    private fun generateBaronessaPoints(): List<Point> {
        return listOf(
            createPoint("p_start", "Punt d'Inici", SAVASSONA_CENTER.latitude, SAVASSONA_CENTER.longitude, PointType.NARRATIVE, 0, false, PointState.VISIBLE, true),
            createPoint("p_baronessa", "La Baronessa", 41.953683, 2.335898, PointType.MANDATORY, 12, true, PointState.LOCKED, true),
            createPoint("p_jove", "El Jove criat", 41.9538348, 2.336077, PointType.MANDATORY, 12, true, PointState.LOCKED, true),
            createPoint("p_espassa", "L'Espassa Màgica", 41.954496, 2.336380, PointType.OBJECT, 30, true, PointState.LOCKED, true), 
            createPoint("p_pastora", "La Pastora", 41.956444, 2.340611, PointType.NARRATIVE, 10, false, PointState.LOCKED, true),
            createPoint("p_baro", "El Baró", 41.953403, 2.336365, PointType.ENEMY, 0, false, PointState.LOCKED, true)
        )
    }

    private fun generateSerpentPoints(): List<Point> {
        return listOf(
            createPoint("s_start", "Plaça de Fra Bernadí", 41.930675, 2.254059, PointType.NARRATIVE, 0, false, PointState.VISIBLE, true),
            createPoint("s_pregoner", "El Pregoner", 41.930467, 2.253944, PointType.NARRATIVE, 0, false, PointState.LOCKED, true),
            createPoint("s_vaquer", "El vaquer", 41.930153, 2.254478, PointType.NARRATIVE, 0, false, PointState.LOCKED, true),
            createPoint("s_rubi", "El rubí de la serpent", 41.930170, 2.254787, PointType.HIDDEN_OBJECT, 0, true, PointState.LOCKED, true),
            createPoint("s_morter", "El Morter", 41.930555, 2.254905, PointType.HIDDEN_OBJECT, 0, true, PointState.LOCKED, true),
            createPoint("s_final", "La plaça", 41.930523, 2.254483, PointType.NARRATIVE, 0, false, PointState.LOCKED, true)
        )
    }

    private fun createPoint(id: String, title: String, lat: Double, lon: Double, type: PointType, score: Int, isKey: Boolean, initialState: PointState = PointState.LOCKED, isAbsolute: Boolean = true): Point {
        return Point(
            id = id,
            title = title,
            coordinate = GeoPoint(lat, lon),
            type = type,
            state = initialState,
            scoreValue = score,
            isKeyItem = isKey
        )
    }
    
    private fun generateBaronessaCards(points: List<Point>): List<Card> {
        return listOf(
            Card("c_start", "Punt d'Inici", "Benvingut! Has desbloquejat l'aventura.", "", "foto_castell_del_baro", true),
            Card("c_baronessa", "La Baronessa", "«El meu espòs mai no ha estat home de cor noble. Els seus gelos ferotges i el seu afany de possessió m’han tinguda captiva entre els murs del castell.»", "Llegenda", "la_baronessa", false),
            Card("c_espassa", "L'Espassa Màgica", "«Aquesta vella espasa guarda el power sagrat de defensar els cors enamorats.»", "Objecte", "la_espassa", false)
        )
    }

    private fun generateSerpentCards(points: List<Point>): List<Card> {
        return listOf(
            Card("c_s_start", "Plaça de Fra Bernadí", "Inici de l'aventura de la Serpent.", "", "serpent_inici", true),
            Card("c_s_rubi", "El rubí de la serpent", "Has trobat el rubí encantat!", "Objecte", "el_rubi", false),
            Card("c_s_morter", "El Morter", "L'arma definitiva contra la serpent.", "Objecte", "serpent_morter", false)
        )
    }
}
