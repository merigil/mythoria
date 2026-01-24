package com.example.mitego.repository

import com.example.mitego.model.Card
import com.example.mitego.model.GameState
import com.example.mitego.model.GameStatus
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.model.PointType
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
import kotlin.random.Random

class GameRepository {

    // Coordenades Base (Savassona)
    private val BASE_LAT = 41.932222
    private val BASE_LON = 2.252583
    
    private val BARO_TIMER_SECONDS = 15 * 60L 
    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    init {
        initializeGame()
    }

    private fun initializeGame() {
        val initialPoints = createPoints()
        _points.value = initialPoints
        _cards.value = createCards()
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }

    private fun createPoints(randomize: Boolean = false): List<Point> {
        fun offset(latOffset: Double, lonOffset: Double): GeoPoint {
            val jitterLat = if (randomize) Random.nextDouble(-0.00004, 0.00004) else 0.0
            val jitterLon = if (randomize) Random.nextDouble(-0.00004, 0.00004) else 0.0
            return GeoPoint(BASE_LAT + latOffset + jitterLat, BASE_LON + lonOffset + jitterLon)
        }

        return listOf(
            Point("p_start", "Punt d'Inici", offset(0.00000, 0.00000), PointType.NARRATIVE, PointState.VISIBLE, score = 0),
            Point("p_baronessa", "La Baronessa", offset(0.00050, 0.00020), PointType.MANDATORY, score = 12, isMandatory = true),
            Point("p_jove", "El Jove", offset(0.00060, -0.00030), PointType.MANDATORY, score = 12, isMandatory = true),
            Point("p_espassa", "L'Espassa", offset(0.00190, 0.00050), PointType.OBJECT, score = 30, isMandatory = true), 
            Point("p_pastora", "La Pastora", offset(0.00080, 0.00080), PointType.NARRATIVE, score = 10),
            Point("p_home_cami", "L'Home del Camí", offset(-0.00040, 0.00040), PointType.NARRATIVE, score = 5),
            Point("p_pont", "Pont Antic", offset(-0.00080, -0.00020), PointType.NARRATIVE, score = 15),
            Point("p_mercader", "Mercader Ambulant", offset(0.00100, -0.00050), PointType.NARRATIVE, score = 15),
            Point("p_pedra", "Pedra Alta", offset(0.00020, -0.00100), PointType.NARRATIVE, score = 8),
            Point("p_moneda1", "Moneda Antiga 1", offset(0.00030, 0.00030), PointType.OBJECT, score = 0), 
            Point("p_moneda2", "Moneda Antiga 2", offset(0.00035, 0.00035), PointType.OBJECT, score = 0),
            Point("p_moneda3", "Moneda Antiga 3", offset(0.00040, 0.00040), PointType.OBJECT, score = 0),
            Point("p_pastor", "El Pastor", offset(-0.00050, -0.00050), PointType.ENEMY, score = -10, isTrap = true),
            Point("p_cavall", "El Cavall", offset(0.00010, 0.00100), PointType.ENEMY, score = -5, isTrap = true),
            Point("p_senyal", "Senyal Camí Ràpid", offset(-0.00020, 0.00120), PointType.ENEMY, score = -15, isTrap = true),
            Point("p_nen", "Nen Trepella", offset(0.00120, 0.00010), PointType.ENEMY, score = -5, isTrap = true),
            Point("p_bota", "Bota de Vi Ranci", offset(-0.00100, 0.00000), PointType.ENEMY, score = -8, isTrap = true),
            Point("p_baro", "El Baró", offset(0.00200, 0.00200), PointType.ENEMY, score = 100, isMandatory = true)
        )
    }

    private fun createCards(): List<Card> {
        return listOf(
            Card("c_start", "Punt d'Inici", "Info", "Benvingut! Has desbloquejat l'aventura.", "placeholder", true),
            Card("c_baronessa", "La Baronessa", "Llegenda", "La Baronessa plora a la font...", "placeholder", true),
            Card("c_jove", "El Jove", "Llegenda", "El jove amant espera...", "placeholder", true),
            Card("c_espassa", "L'Espassa Màgica", "Objecte", "Una espasa antiga capaç de vèncer al Baró.", "placeholder", true),
            Card("c_baro", "El Baró de Savassona", "Enemic", "El Baró arriba furiós! Tens 15 minuts per trobar la Baronessa i el Jove!", "placeholder", true),
            Card("c_pastora", "La Pastora", "Llegenda", "La Pastora coneix els secrets del bosc. (+10 Punts)", "placeholder", true),
            Card("c_home_cami", "L'Home del Camí", "Llegenda", "Un viatger misteriós et dona consells. (+5 Punts)", "placeholder", true),
            Card("c_pont", "Pont Antic", "Lloc", "Un pont ple d'història. (+15 Punts)", "placeholder", true),
            Card("c_mercader", "Mercader Ambulant", "Llegenda", "Et ven informació valuosa. (+15 Punts)", "placeholder", true),
            Card("c_pedra", "Pedra Alta", "Lloc", "Una pedra ritual antiga. (+8 Punts)", "placeholder", true),
            Card("c_moneda1", "Moneda Antiga 1", "Objecte", "Una moneda d'or perduda. (+0 Punts)", "placeholder", true),
            Card("c_moneda2", "Moneda Antiga 2", "Objecte", "Una moneda d'or perduda. (+0 Punts)", "placeholder", true),
            Card("c_moneda3", "Moneda Antiga 3", "Objecte", "Una moneda d'or perduda. (+0 Punts)", "placeholder", true),
            Card("c_pastor", "El Pastor", "Trampa", "El pastor t'enganya i et fa perdre temps! (-10 Punts)", "placeholder", true),
            Card("c_cavall", "El Cavall", "Trampa", "El cavall s'espanta i has de fugir. (-5 Punts)", "placeholder", true),
            Card("c_senyal", "Senyal Camí Ràpid", "Trampa", "El senyal senyalava un camí fals! (-15 Punts)", "placeholder", true),
            Card("c_nen", "Nen Trepella", "Trampa", "Et roba algunes monedes (simbòlic). (-5 Punts)", "placeholder", true),
            Card("c_bota", "Bota de Vi Ranci", "Trampa", "T'adorms una estona... (-8 Punts)", "placeholder", true)
        )
    }

    fun getCard(cardId: String): Card {
        return _cards.value.find { it.id == cardId } 
            ?: Card(cardId, "Indret Desconegut", "Misteri", "Has trobat un lloc misteriós...", "placeholder", true)
    }

    fun updatePointState(pointId: String, newState: PointState) {
        _points.update { list ->
            list.map { if (it.id == pointId) it.copy(state = newState) else it }
        }
    }

    fun onPointVisited(pointId: String) {
        val currentPoints = _points.value
        val point = currentPoints.find { it.id == pointId } ?: return
        
        if (point.state == PointState.COMPLETED || point.state == PointState.LOCKED) return

        updatePointState(pointId, PointState.COMPLETED)

        _gameState.update { state ->
            val newScore = state.totalScore + point.score
            val newInventory = state.inventory.toMutableSet()
            val newVisited = state.visitedPoints.toMutableSet().apply { add(pointId) }
            
            if (pointId == "p_espassa") newInventory.add("espassa")
            
            var newStatus = state.status
            if (pointId == "p_start" && state.status == GameStatus.WAITING_TO_START) {
                newStatus = GameStatus.ACTIVE_PLAY
                unlockAllPoints()
            } else if (pointId == "p_baro") {
                newStatus = GameStatus.BARO_CHALLENGE
                startBaroTimer()
            }

            state.copy(
                totalScore = if (pointId.startsWith("p_moneda") && newVisited.count { it.startsWith("p_moneda") } == 3) newScore + 5 else newScore,
                inventory = newInventory,
                status = newStatus,
                visitedPoints = newVisited
            )
        }
    }

    private fun unlockAllPoints() {
        _points.update { list ->
            list.map { 
                if (it.state == PointState.LOCKED) it.copy(state = PointState.VISIBLE) else it 
            }
        }
    }

    private fun startBaroTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = BARO_TIMER_SECONDS) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { 
                    val remaining = (it.timerSecondsRemaining ?: 0) - 1
                    it.copy(timerSecondsRemaining = remaining)
                }
                checkWinCondition() 
            }
            checkWinCondition(timeExpired = true)
        }
    }
    
    fun checkWinCondition(timeExpired: Boolean = false) {
        val state = _gameState.value
        val hasSword = state.inventory.contains("espassa")
        val hasScore = state.totalScore >= 100
        val hasBaronessa = state.visitedPoints.contains("p_baronessa")
        val hasJove = state.visitedPoints.contains("p_jove")

        if (timeExpired) {
             if (hasSword && hasScore && hasBaronessa && hasJove) {
                 _gameState.update { it.copy(status = GameStatus.WON) }
             } else {
                 _gameState.update { it.copy(status = GameStatus.LOST) }
             }
        } else {
            if (state.status == GameStatus.BARO_CHALLENGE && hasSword && hasScore && hasBaronessa && hasJove) {
                 _gameState.update { it.copy(status = GameStatus.WON, timerSecondsRemaining = null) }
                 timerJob?.cancel()
            }
        }
    }

    fun restartGame() {
        timerJob?.cancel()
        val randomPoints = createPoints(randomize = true)
        _points.value = randomPoints
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }
}
